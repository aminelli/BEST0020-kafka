# Installazione portainer


Aprire un prompt dei comandi e lanciare:

```shell
docker volume create portainer_data
docker run -d -p 8900:8000 -p 9443:9443 --name portainer --restart=always -v /var/run/docker.sock:/var/run/docker.sock -v portainer_data:/data portainer/portainer-ce:lts
```

Una volta eseguito il comando aprire il browser e inserire il seguente URL:

- https://localhost:9443