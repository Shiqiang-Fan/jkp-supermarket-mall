FROM 192.168.23.144/library/nginx
ENV conf_path /etc/nginx/conf.d/
RUN mkdir -p /opt/redeem/dist
COPY dist /opt/redeem/dist
COPY nginx/ ${conf_path}
COPY nginx.conf /etc/nginx/
