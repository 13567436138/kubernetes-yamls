#!/bin/bash

set -o errexit
tmp_dir=$(mktemp -d)

usage() {
    cat - <<-EOF
		usage: $0 <backup-name> <cluster-name>

		OPTIONS:
		    <backup-name>  the backup name
		                   it can be obtained with the "kubectl get pxc-backup" command
		    <cluster-name> the name of an existing Percona XtraDB Cluster
		                   it can be obtained with the "kubectl get pxc" command
	EOF
    exit 1
}

get_backup_pvc() {
    local backup=$1

    if kubectl get "pxc-backup/$backup" 1>/dev/null 2>/dev/null; then
        kubectl get "pxc-backup/$backup" -o jsonpath='{.status.volume}'
    else
        # support direct PVC name here
        echo -n "$backup"
    fi
}

enable_logging() {
    BASH_VER=$(echo "$BASH_VERSION" | cut -d . -f 1,2)
    if (( $(echo "$BASH_VER >= 4.1" |bc -l) )); then
        exec 5>"$tmp_dir/log"
        BASH_XTRACEFD=5
        set -o xtrace
        echo "Log: $tmp_dir/log"
    fi
}

check_input() {
    local backup_pvc=$1
    local cluster=$2

    if [ -z "$backup_pvc" ] || [ -z "$cluster" ]; then
        usage
    fi

    if ! kubectl get "pxc/$cluster" 1>/dev/null; then
        printf "[ERROR] '%s' Percona XtraDB Cluster doesn't exists.\n\n" "$cluster"
        usage
    fi

    if ! kubectl get "pvc/$backup_pvc" 1>/dev/null; then
        printf "[ERROR] '%s' PVC doesn't exists.\n\n" "$backup_pvc"
        usage
    fi

    echo "All data in '$cluster' Percona XtraDB Cluster will be deleted during the backup restoration."
    while read -r -p "Are you sure? [y/N] " answer; do
        case "$answer" in
            [Yy]|[Yy][Ee][Ss]) break;;
            ""|[Nn]|[Nn][Oo])  exit 0;;
        esac
    done
}

stop_pxc() {
    local cluster=$1
    local size
    size=$(kubectl get "pxc/$cluster" -o jsonpath='{.spec.pxc.size}')

    kubectl get "pxc/$cluster" -o yaml > "$tmp_dir/cluster.yaml"
    kubectl delete -f "$tmp_dir/cluster.yaml"

    for i in $(seq 0 "$((size-1))" | sort -r); do
        echo -n "Deleting $cluster-pxc-node-$i."
        until (kubectl get "pod/$cluster-pxc-node-$i" || :) 2>&1 | grep -q NotFound; do
            sleep 1
            echo -n .
        done
        echo "[done]"
    done

    if [ "$size" -gt 1 ]; then
        for i in $(seq 1 "$((size-1))" | sort -r); do
            kubectl delete "pvc/datadir-$cluster-pxc-node-$i"
        done
    fi
}

start_pxc() {
    kubectl apply -f "$tmp_dir/cluster.yaml"
}

recover() {
    local backup_pvc=$1
    local cluster=$2

    kubectl delete "job/xtrabackup-restore-job-$cluster" 2>/dev/null || :
    cat - <<-EOF | kubectl apply -f -
		apiVersion: batch/v1
		kind: Job
		metadata:
		  name: xtrabackup-restore-job-$cluster
		spec:
		  template:
		    spec:
		      containers:
		      - name: xtrabackup
		        image: perconalab/backupjob-openshift
		        command:
		        - bash
		        - "-exc"
		        - |
		          cd /backup
		          md5sum -c md5sum.txt

		          rm -rf /datadir/*
		          cat /backup/xtrabackup.stream | xbstream -x -C /datadir
		          xtrabackup --prepare --target-dir=/datadir
		        volumeMounts:
		        - name: backup
		          mountPath: /backup
		        - name: datadir
		          mountPath: /datadir
		      restartPolicy: Never
		      volumes:
		      - name: backup
		        persistentVolumeClaim:
		          claimName: $backup_pvc
		      - name: datadir
		        persistentVolumeClaim:
		          claimName: datadir-$cluster-pxc-node-0
		  backoffLimit: 4
	EOF

    echo -n "Recovering."
    until kubectl get "job/xtrabackup-restore-job-$cluster" -o jsonpath='{.status.completionTime}' 2>/dev/null | grep -q 'T'; do
        sleep 1
        echo -n .
    done
    echo "[done]"
}

main() {
    local backup=$1
    local cluster=$2
    local backup_pvc

    enable_logging
    backup_pvc=$(get_backup_pvc "$backup")
    check_input "$backup_pvc" "$cluster"

    stop_pxc "$cluster"
    recover "$backup_pvc" "$cluster"
    start_pxc

    cat - <<-EOF

		You can view xtrabackup log:
		    $ kubectl logs job/xtrabackup-restore-job-$cluster
		If everything is fine, you can cleanup the job:
		    $ kubectl delete job/xtrabackup-restore-job-$cluster

	EOF
}

main "$@"
exit 0
