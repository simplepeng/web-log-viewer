# 使用官方 Nginx 镜像作为基础
FROM nginx:latest

# 删除默认的 Nginx 网站内容
RUN rm -rf /usr/share/nginx/html/*

# 将构建好的 Compose Web 应用文件复制到 Nginx 托管目录
COPY docs /usr/share/nginx/html

# （可选）如果需要自定义 Nginx 配置，可以复制配置文件
# COPY nginx.conf /etc/nginx/conf.d/default.conf

# 复制自定义的 MIME 类型配置文件到 Nginx 的 conf.d 目录
COPY wasm-mime.conf /etc/nginx/conf.d/wasm-mime.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]