FROM nginx:1.27.4

COPY ./entrypoint.sh /docker-entrypoint.d/00-generate-ssl.sh
RUN chmod +x /docker-entrypoint.d/00-generate-ssl.sh
