#!/bin/bash

pnpm build
rsync -rvz --numeric-ids --usermap=:33 --groupmap=:33 --delete-after ./dist/* norma:/srv/http/yunpan
