FROM jetty:9.4-jre8-alpine
ADD start.ini /var/lib/jetty/start.ini
USER root
RUN apk update
RUN apk add ttf-dejavu
ENV TIME_ZONE Asia/Shanghai
RUN apk add --no-cache tzdata \
    && echo "${TIME_ZONE}" > /etc/timezone \
    && ln -sf /usr/share/zoneinfo/${TIME_ZONE} /etc/localtime
USER jetty
