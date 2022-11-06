cd /var/jenkins_home/workspace/COLLECT-backend
ls -al
docker stop collect-backend
docker rm collect-backend
docker build . -t collect-backend --no-cache
docker run -d --name collect-backend -p 8765:8765 --restart unless-stopped  collect-backend:latest
exit
exit
