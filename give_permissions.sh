docker system prune -a
cd .docker-volumes/mysql/db_data/
sudo chmod 777 ./*
cd ../../..
sudo chmod 777 ./*
docker-compose up --build