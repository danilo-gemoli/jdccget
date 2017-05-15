#!/usr/bin/env bash

function create_dir_if_not_exists {
    if [ ! -d "$1" ]; then
        mkdir "$1"
    fi
}

function copy_dir_if_not_exists {
    if [ ! -d "$2" ]; then
        cp -R "$1" "$2"
    fi
}

function copy_if_not_exists {
    if [ ! -f "$2" ]; then
        cp "$1" "$2"
    fi
}

function is_host_reachable {
    ping -q -w 3 -c 3 $1 > /dev/null
}