

I don't expect anyone to contribute to this so here are some notes for future terry.

---
### Code Terms:
- ModuleName: `:dir:dir`
- ModulePath = `/dir/dir`

### Gradle run config example values:
```
    - Name : String -> "gradle-harpoon: [build]"
    - cmdline : String -> "build"
    - externalPath : String -> "$PROJECT_DIR$"
    - runAsTest : Boolean -> false
```
```
    - Name : String -> "advent-of-code:2015:day09 [run]"
    - cmdline : String -> "run"
    - externalPath : String -> "$PROJECT_DIR$/src/2015/day09"
    - runAsTest : Boolean -> false
```
---
Update the version in gradle.properties before cutting a tag.
