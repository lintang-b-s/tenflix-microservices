#!/bin/bash

if [ $# -ne 1 ]; then
  echo "Usage: git-commit-size.sh <commit hash>" 1>&2
  exit 1
fi

HASH=$1

ITEM_LIST="`git diff-tree -r -c -M -C --no-commit-id $HASH`"
BLOB_HASH_LIST="`echo "$ITEM_LIST" | awk '{ print $4 }'`"
SIZE_LIST="`echo "$BLOB_HASH_LIST" | git cat-file --batch-check | grep "blob" | awk '{ print $3}'`"
COMMIT_SIZE="`echo "$SIZE_LIST" | awk '{ sum += $1 } END { print sum }'`"
echo "$COMMIT_SIZE"
