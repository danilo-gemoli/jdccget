#!/usr/bin/env bash

. common.sh
. names.sh

# Crea la struttura della cartella package.
create_dir_if_not_exists "${PACK_ROOT}"
create_dir_if_not_exists "${PACK_LIBS}"
create_dir_if_not_exists "${PACK_APP_BIN_ROOT}"
create_dir_if_not_exists "${PACK_SCRIPT_PATH}"

### Compilato dell'applicazione ###
copy_dir_if_not_exists "${APP_BIN_ROOT}/${APP_FOLDERNAME}" "${PACK_APP_BIN_ROOT}/${APP_FOLDERNAME}"

### Configurazione ###
copy_dir_if_not_exists "${ROOT}/${CONFIG_FILENAME}" "${PACK_ROOT}/${CONFIG_FILENAME}"

### Script ###
copy_if_not_exists "${SCRIPT_PATH}/${NAMES_SCRIPT_FALENAME}" "${PACK_SCRIPT_PATH}/${NAMES_SCRIPT_FALENAME}"
copy_if_not_exists "${SCRIPT_PATH}/${RUN_SCRIPT_FILENAME}" "${PACK_ROOT}/${RUN_SCRIPT_FILENAME}"

### Librerie ###
# Logback
copy_if_not_exists "${LOGBACK_PATH}/${LOGBACK_CLASSIC_FILENAME}" "${PACK_LIBS}/${LOGBACK_CLASSIC_FILENAME}"
copy_if_not_exists "${LOGBACK_PATH}/${LOGBACK_CORE_FILENAME}" "${PACK_LIBS}/${LOGBACK_CORE_FILENAME}"

# Dipendenze pircbotx
# Commons
copy_if_not_exists "${COMMONS_LANG_PATH}/${COMMONS_LANG_FILENAME}" "${PACK_LIBS}/${COMMONS_LANG_FILENAME}"
copy_if_not_exists "${COMMONS_CODEC_PATH}/${COMMONS_CODEC_FILENAME}" "${PACK_LIBS}/${COMMONS_CODEC_FILENAME}"
# Guava
copy_if_not_exists "${PIRCBOTX_DEPENDENCIES_PATH}/${GUAVA_FILENAME}" "${PACK_LIBS}/${GUAVA_FILENAME}"
# JSR
copy_if_not_exists "${PIRCBOTX_DEPENDENCIES_PATH}/${JSR_FILENAME}" "${PACK_LIBS}/${JSR_FILENAME}"
# Lombok
copy_if_not_exists "${PIRCBOTX_DEPENDENCIES_PATH}/${LOMBOK_FILENAME}" "${PACK_LIBS}/${LOMBOK_FILENAME}"
# Slf4j
copy_if_not_exists "${SLF4J_PATH}/${SLF4J_API_FILENAME}" "${PACK_LIBS}/${SLF4J_API_FILENAME}"