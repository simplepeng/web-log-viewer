docker container rm web-log-viewer
docker run -d -p 8083:80 --name web-log-viewer web-log-viewer:latest