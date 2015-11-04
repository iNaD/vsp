## How to use the Docker Image
1. Make a package of the current version `mvn package`.

2. Build an image of the current version `docker build -f PATH/TO/Dockerfile .`

3. Running the image in a container `docker run -d -p 4567:4567 IMAGE_ID`

4. Now you should be able to access the REST API on `127.0.0.1:4567` (Linux) or `192.168.99.100:4567` (Windows)
