# Contributor Branch Integration Plan

This repository currently has only one local branch (`work`) and no configured remotes.
So to merge all contributor work into `main`, first fetch branches from your GitHub remote, then merge each non-merged branch with normal merge commits.

## 1) Analyze branch structure

```bash
git remote -v
git fetch --all --prune
git branch -a -vv
git log --graph --decorate --oneline --all --date-order
```

## 2) Ensure local `main` tracks `origin/main`

```bash
git checkout main || git checkout -b main origin/main
git pull --ff-only origin main
```

## 3) Identify branches not merged into `main`

```bash
# Local branches not merged into main
git branch --no-merged main

# Remote contributor branches not merged into origin/main
git branch -r --no-merged origin/main \
  | sed 's/^[ *]*//' \
  | grep -v '^origin/main$'
```

## 4) Merge each contributor branch (normal merge, no squash)

```bash
# Example loop for all unmerged remote branches except origin/main
git checkout main
for b in $(git branch -r --no-merged origin/main | sed 's/^[ *]*//' | grep -v '^origin/main$'); do
  echo "Merging $b"
  git merge --no-ff "$b"
done
```

> Do not use `--squash`, do not rebase branches before merge, and do not amend author metadata.

## 5) Resolve conflicts safely (if any)

When a merge stops with conflicts:

```bash
git status
# edit conflicted files, then
git add <resolved-file-1> <resolved-file-2>
git commit
```

If you need to stop and retry:

```bash
git merge --abort
```

## 6) Verify authorship was preserved

```bash
git log --format='%h %an <%ae> %s' main
git shortlog -sne main
```

## 7) Push updated `main`

```bash
git push origin main
```

## 8) Confirm GitHub contribution counting

Contributions will count when all of the following are true:

1. The commits are present on the repository's default branch (`main`) or `gh-pages`.
2. Commit author email matches a verified email on each contributor's GitHub account.
3. The commits are not made with bot/no-reply addresses that are not linked to contributor profiles.

Useful check command:

```bash
git log main --format='%h %an <%ae>' | sort -u
```

Have each contributor verify every email shown is added + verified in GitHub account settings.
