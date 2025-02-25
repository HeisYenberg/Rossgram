#!/bin/sh

mkdir -p /etc/nginx/ssl

if [ ! -f /etc/nginx/ssl/rossgram.local.crt ]; then
  openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
    -keyout /etc/nginx/ssl/rossgram.local.key \
    -out /etc/nginx/ssl/rossgram.local.crt \
    -subj "/C=RU/ST=Tatarstan/L=Kazan/O=HeisYenberg/CN=heisyenberg.rossgram"
fi

exec nginx -g "daemon off;"
