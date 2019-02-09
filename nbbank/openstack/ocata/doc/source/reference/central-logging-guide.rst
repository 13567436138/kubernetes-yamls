.. _central-logging-guide:

========================
Central Logging in Kolla
========================

An OpenStack deployment generates vast amounts of log data. In order to
successfully monitor this and use it to diagnose problems, the standard "ssh
and grep" solution quickly becomes unmanageable.

Preparation and deployment
==========================

Modify the configuration file ``/etc/kolla/globals.yml`` and change
the following:

::

    enable_central_logging: "yes"

Elasticsearch
=============

Kolla deploys Elasticsearch as part of the E*K stack to store, organize
and make logs easily accessible.

By default Elasticsearch is deployed on port ``9200``.

.. note::

    Elasticsearch stores a lot of logs, so if you are running centralized logging,
    remember to give ``/var/lib/docker`` an adequate space.


Kibana
======

Kolla deploys Kibana as part of the E*K stack in order to allow operators to
search and visualise logs in a centralised manner.

After successful deployment, Kibana can be accessed using a browser on
``<kolla_external_vip_address>:5601``.

The default username is ``kibana``, the password can be located under
``<kibana_password>`` in ``/etc/kolla/passwords.yml``.

When Kibana is opened for the first time, it requires creating a default index
pattern. To view, analyse and search logs, at least one index pattern has to be
created. To match indices stored in ElasticSearch, we suggest setting the
"Index name or pattern" field to ``log-*``. The rest of the fields can be left
as is.

After setting parameters, create an index by clicking the ``Create`` button.

.. note:: This step is necessary until the default Kibana dashboard is implemented
          in Kolla.

Search logs - Discover tab
--------------------------

Operators can create and store searches based on various fields from logs, for
example, "show all logs marked with ERROR on nova-compute".

To do this, click the ``Discover`` tab. Fields from the logs can be filtered by
hovering over entries from the left hand side, and clicking ``add`` or
``remove``. Add the following fields:

* Hostname
* Payload
* severity_label
* programname

This yields an easy to read list of all log events from each node in the
deployment within the last 15 minutes. A "tail like" functionality can be
achieved by clicking the clock icon in the top right hand corner of the screen,
and selecting ``Auto-refresh``.

Logs can also be filtered down further. To use the above example, type
``programname:nova-compute`` in the search bar. Click the drop-down arrow from
one of the results, then the small magnifying glass icon from beside the
programname field. This should now show a list of all events from nova-compute
services across the cluster.

The current search can also be saved by clicking the ``Save Search`` icon
available from the menu on the right hand side.

Example: using Kibana to diagnose a common failure
--------------------------------------------------

The following example demonstrates how Kibana can be used to diagnose a common
OpenStack problem, where an instance fails to launch with the error 'No valid
host was found'.

First, re-run the server creation with ``--debug``:

::

  openstack --debug server create --image cirros --flavor m1.tiny \
  --key-name mykey --nic net-id=00af016f-dffe-4e3c-a9b8-ec52ccd8ea65 \
  demo1

In this output, look for the key ``X-Compute-Request-Id``. This is a unique
identifier that can be used to track the request through the system. An
example ID looks like this:

::

    X-Compute-Request-Id: req-c076b50a-6a22-48bf-8810-b9f41176a6d5

Taking the value of ``X-Compute-Request-Id``, enter the value into the Kibana
search bar, minus the leading ``req-``. Assuming some basic filters have been
added as shown in the previous section, Kibana should now show the path this
request made through the OpenStack deployment, starting at a ``nova-api`` on
a control node, through the ``nova-scheduler``, ``nova-conductor``, and finally
``nova-compute``. Inspecting the ``Payload`` of the entries marked ``ERROR``
should quickly lead to the source of the problem.

While some knowledge is still required of how Nova works in this instance, it
can still be seen how Kibana helps in tracing this data, particularly in a
large scale deployment scenario.

Visualize data - Visualize tab
------------------------------

In the visualization tab a wide range of charts is available. If any
visualization has not been saved yet, after choosing this tab *Create a new
visualization* panel is opened. If a visualization has already been saved,
after choosing this tab, lately modified visualization is opened. In this
case, one can create a new visualization by choosing *add visualization*
option in the menu on the right. In order to create new visualization, one
of the available options has to be chosen (pie chart, area chart). Each
visualization can be created from a saved or a new search. After choosing
any kind of search, a design panel is opened. In this panel, a chart can be
generated and previewed. In the menu on the left, metrics for a chart can
be chosen. The chart can be generated by pressing a green arrow on the top
of the left-side menu.

.. note:: After creating a visualization, it can be saved by choosing *save
   visualization* option in the menu on the right. If it is not saved, it
   will be lost after leaving a page or creating another visualization.

Organize visualizations and searches - Dashboard tab
----------------------------------------------------

In the Dashboard tab all of saved visualizations and searches can be
organized in one Dashboard. To add visualization or search, one can choose
*add visualization* option in the menu on the right and then choose an item
from all saved ones. The order and size of elements can be changed directly
in this place by moving them or resizing. The color of charts can also be
changed by checking a colorful dots on the legend near each visualization.

.. note:: After creating a dashboard, it can be saved by choosing *save dashboard*
   option in the menu on the right. If it is not saved, it will be lost after
   leaving a page or creating another dashboard.

If a Dashboard has already been saved, it can be opened by choosing *open
dashboard* option in the menu on the right.

Exporting and importing created items - Settings tab
----------------------------------------------------

Once visualizations, searches or dashboards are created, they can be exported
to a JSON format by choosing Settings tab and then Objects tab. Each of the
item can be exported separately by selecting it in the menu. All of the items
can also be exported at once by choosing *export everything* option.
In the same tab (Settings - Objects) one can also import saved items by
choosing *import* option.
