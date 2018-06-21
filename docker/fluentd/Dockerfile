FROM registry.cn-hangzhou.aliyuncs.com/hxpdocker/fluentd-elasticsearch:v2.0.4
RUN gem sources --add https://gems.ruby-china.org/ --remove https://rubygems.org/
RUN fluent-gem install fluent-plugin-mysqlslowquery