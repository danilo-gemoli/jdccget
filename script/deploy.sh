#!/usr/bin/env bash

. common.sh
. names.sh

# Se un deploy è già stato fatto allora ne esegue il backup.
ssh -q ${DEPLOY_REMOTE_USER}@${DEPLOY_REMOTE_HOST} << EOF
    if [ -d "${DEPLOY_APP_PATH}" ]; then
        if [ -d "${DEPLOY_APP_BACKUP_PATH}" ]; then
            rm -rf "${DEPLOY_APP_BACKUP_PATH}"
        fi
        mv "${DEPLOY_APP_PATH}" "${DEPLOY_APP_BACKUP_PATH}"
    fi
EOF

scp -r "${PACK_ROOT}" ${DEPLOY_REMOTE_USER}@${DEPLOY_REMOTE_HOST}:"${DEPLOY_APP_PATH}"