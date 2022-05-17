#!/bin/sh
#
# Script to run docker dispatcher image to detect changes in immutable config files
# or to extract immutable config files from the image into a folder.
#
# Usage: docker_immutability_check.sh config-folder [mode: {check|extract}]
#

usage() {
    cat <<EOF >& 1
Usage: $0 config-folder [mode: {check|extract}]

Examples:
  # Use config folder "src" (assuming default "check" mode) for immutable files check
  $0 src
  or explicitly giving the "check" mode
  $0 src check
  # Use config folder "src" as a destination to extract immutable files into it
  $0 src extract

EOF
    exit 1
}

error() {
    echo >&2 "** error: $1"
    exit 2
}

[ $# -eq 1 ] || [ $# -eq 2 ] || usage

folder=$1
shift

mode=$1

if [ "$mode" == "" ]; then
	echo "empty mode param, assuming mode = 'check'"
	mode="check"
else
	if [ "$mode" != "check" ] && [ "$mode" != "extract" ]; then
		error "mode '$mode' is neither 'check' nor 'extract'"
	fi
fi

echo "running in '${mode}' mode"

command -v docker >/dev/null 2>&1 || error "docker not found, aborting."

# Make folder path absolute for docker volume mount
first=$(echo "${folder}" | sed 's/\(.\).*/\1/')
if [ "${first}" != "/" ]
then
    folder=${PWD}/${folder}
fi

[ -d "${folder}" ] || error "config folder not found: ${folder}"

repo=adobe
image=aem-ethos/dispatcher-publish
version=2.0.88
imageurl="${repo}/${image}:${version}"

if [ -z "$(docker images -q "${imageurl}" 2> /dev/null)" ]; then
    echo "Required image not found, trying to load from archive..."
    file=$(dirname "$0")/dispatcher-publish.tar.gz
    [ -f "${file}" ] || error "unable to find archive at expected location: $file"
    gunzip -c "${file}" | docker load
    [ -n "$(docker images -q "${imageurl}" 2> /dev/null)" ] || error "required image still not found: $imageurl"
fi

scriptDir="$(cd -P "$(dirname "$0")" && pwd -P)"

configVolumeMountMode="rw"
if [ "$mode" == "check" ]; then
    configVolumeMountMode="ro"
fi

docker run --rm \
    -v "${folder}":/etc/httpd-actual:${configVolumeMountMode} \
    -v "${scriptDir}"/immutability_check.sh:/usr/sbin/immutability_check.sh:ro \
    --entrypoint /bin/sh "${imageurl}" /usr/sbin/immutability_check.sh /etc/httpd/immutable.files.txt /etc/httpd-actual ${mode}
