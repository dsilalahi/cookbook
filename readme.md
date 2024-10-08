### Personal scripts/notes collection
This repository has a comprehensive collection of scripts, code snippets, and technical notes gathered from numerous projects that I've worked on over the years. Whether you need a quick answer to a common problem, a reference to better grasp a specific technology, or insights into difficult technological challenges, you'll find them here.  Some of these scripts might be outdated. 


```
git remote -v # remote verbs
git remote rename origin {new name} # update remote verbs
```

### mirroring
```
git clone —bare old-repo.git
cd old-repo.git
git push —mirror new-repo.git
cd ..
rm -rf old-repo.git
```

### add files to git
```
git add file(s)
git commit -m "comments"
git push origin master
```

### remove files to git
```
git rm file(s)
git commit -m "comments"
git push origin master
```

### delete remote files but not local
```
git rm --cached file1
git commit -m "remove file1"
git push origin master
```

### take the current branch and points it to the HEAD of the remote branch.
```
git fetch origin
git reset --hard origin/master
```