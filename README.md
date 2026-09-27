# OpenJML Demo

I assume Linux as per lab machines.

## Install OpenJML
1. Download the Linux zip from <https://www.openjml.org/downloads.html>.
2. Unzip to `~/tools/openjml` (any folder is fine):
   ```bash
   mkdir -p ~/tools
   cd ~/tools
   unzip openjml-*.zip -d openjml
   ```
3. Add to your shell profile (bash example):
   ```bash
   echo 'export OJ="$HOME/tools/openjml"' >> ~/.bashrc
   echo 'export PATH="$OJ:$PATH"' >> ~/.bashrc
   source ~/.bashrc
   ```
4. Test:
   ```bash
   openjml --version
   ```

## Static Verification (ESC)

```bash
openjml -esc --progress --show-summary Clock24Jml.java
```

See [Clock explanation](clock_explanation.md).

## Runtime Assertion Checking (RAC)

```bash
openjml -rac Clock24Jml.java
```

## Execute

```bash
openjml-java -cp ".:$HOME/tools/openjml/jmlruntime.jar" -ea Clock24Jml 
```
