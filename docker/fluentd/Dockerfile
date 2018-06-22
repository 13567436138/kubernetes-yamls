FROM registry.cn-hangzhou.aliyuncs.com/hxpdocker/fluentd-elasticsearch:v2.0.4
RUN gem sources --add https://gems.ruby-china.org/ --remove https://rubygems.org/
RUN fluent-gem install fluent-plugin-mysqlslowquery
RUN fluent-gem  install fluent-plugin-glusterfs
RUN fluent-gem install fluent-plugin-esslowquery