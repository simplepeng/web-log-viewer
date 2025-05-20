# 使用官方 Nginx 镜像作为基础
FROM nginx:alpine
# 删除默认的 Nginx 网站内容
RUN rm -rf /usr/share/nginx/html/*
# 将构建好的 Compose Web 应用文件复制到 Nginx 托管目录
COPY docs /usr/share/nginx/html
# （可选）如果需要自定义 Nginx 配置，可以复制配置文件
# COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]