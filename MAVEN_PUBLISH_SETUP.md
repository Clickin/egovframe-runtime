# Maven Publishing to GitHub Packages Setup

This document explains how to use the GitHub Actions workflow to publish eGovFrame Runtime packages to GitHub Packages.

## 🎯 Overview

The project is now configured to publish Maven artifacts to GitHub Packages using GitHub Actions. This setup supports:

- **Version-aware Java selection**: Java 8 for v4.x, Java 17 for v5.0+
- **Manual publishing**: Controlled via GitHub Actions UI
- **Local testing**: Using nektos/act
- **SNAPSHOT support**: For development versions

## 📦 Published Artifacts

When published, the following artifacts will be available in GitHub Packages:

### Foundation Layer (12 modules)
- `org.egovframe.rte:org.egovframe.rte.fdl.access`
- `org.egovframe.rte:org.egovframe.rte.fdl.cmmn`
- `org.egovframe.rte:org.egovframe.rte.fdl.crypto`
- `org.egovframe.rte:org.egovframe.rte.fdl.excel`
- `org.egovframe.rte:org.egovframe.rte.fdl.filehandling`
- `org.egovframe.rte:org.egovframe.rte.fdl.idgnr`
- `org.egovframe.rte:org.egovframe.rte.fdl.logging`
- `org.egovframe.rte:org.egovframe.rte.fdl.property`
- `org.egovframe.rte:org.egovframe.rte.fdl.reactive`
- `org.egovframe.rte:org.egovframe.rte.fdl.security`
- `org.egovframe.rte:org.egovframe.rte.fdl.string`
- `org.egovframe.rte:org.egovframe.rte.fdl.xml`

### Other Layers
- **Batch**: `org.egovframe.rte.bat.core`
- **Integration**: `org.egovframe.rte.itl.integration`, `org.egovframe.rte.itl.webservice`
- **Persistence**: 6 modules (`psl.*`)
- **Presentation**: 3 modules (`ptl.*`)

## 🚀 How to Publish

### For Repository Maintainers:

1. **Go to Actions tab** in the GitHub repository
2. **Select "Publish to GitHub Packages"** workflow
3. **Click "Run workflow"**
4. **Configure options**:
   - **Version**: Enter version (e.g., `4.3.0`, `4.3.1-SNAPSHOT`, `5.0.0`)
   - **Skip tests**: Check if you want to skip tests (faster build)
5. **Click "Run workflow"** to start publishing

### Version Examples:
- `4.3.0` → Uses Java 8, publishes stable release
- `4.3.1-SNAPSHOT` → Uses Java 8, publishes development snapshot
- `5.0.0` → Uses Java 17, publishes v5.0 with Spring 6

## 🧪 Local Testing

Before pushing workflow changes, test locally using nektos/act:

### Prerequisites:
1. **Install act**:
   ```bash
   choco install act-cli
   # or
   winget install nektos.act
   ```

2. **Create secrets file**:
   ```bash
   copy .secrets.act.example .secrets.act
   # Edit .secrets.act and add your GitHub Personal Access Token
   ```

3. **Create Personal Access Token**:
   - Go to: https://github.com/settings/tokens
   - Required scopes: `write:packages`, `read:packages`

### Run Tests:
```batch
# Test the workflow (dry run)
act-test.bat

# For real testing (remove --dryrun from act-test.bat)
```

## 📥 Consuming Published Packages

### For Developers Using the Packages:

1. **Configure Maven settings**:
   ```bash
   # Copy example settings
   copy settings.xml.example ~/.m2/settings.xml

   # Edit ~/.m2/settings.xml:
   # - Replace YOUR_GITHUB_USERNAME with your GitHub username
   # - Replace YOUR_PERSONAL_ACCESS_TOKEN with your token
   ```

2. **Add dependencies** in your project's `pom.xml`:
   ```xml
   <dependency>
       <groupId>org.egovframe.rte</groupId>
       <artifactId>org.egovframe.rte.fdl.cmmn</artifactId>
       <version>4.3.0</version>
   </dependency>
   ```

3. **Use the packages** in your Java code:
   ```java
   import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;

   public class MyService extends EgovAbstractServiceImpl {
       // Your implementation
   }
   ```

## 🔧 Files Modified/Created

### Modified Files:
- **`pom.xml`**: Added `distributionManagement` section
- **`.gitignore`**: Added act testing files to ignore list

### New Files:
- **`.github/workflows/maven-publish.yml`**: Main publishing workflow
- **`.actrc`**: Configuration for local act testing
- **`.secrets.act.example`**: Example secrets file for local testing
- **`act-test.bat`**: Windows script for testing workflow locally
- **`settings.xml.example`**: Example Maven settings for consuming packages

## 🔐 Security

- **No secrets needed in repository**: Uses GitHub's built-in `GITHUB_TOKEN`
- **Local testing secrets**: `.secrets.act` is git-ignored
- **Personal Access Tokens**: Required only for consuming packages

## ⚠️ Important Notes

1. **Java Version Strategy**:
   - v4.x versions use Java 8
   - v5.0+ versions will use Java 17
   - Workflow automatically detects and uses correct version

2. **Package Visibility**:
   - Packages are published to GitHub Packages
   - Public repository = public packages
   - Requires authentication even for public packages

3. **SNAPSHOT Versions**:
   - Supported for development releases
   - Use format: `4.3.1-SNAPSHOT`

4. **No GPG Signing**:
   - GitHub Packages doesn't require GPG signing
   - Simpler than Maven Central publishing

## 🆘 Troubleshooting

### Common Issues:

1. **Workflow fails during testing**:
   - Check if tests pass locally: `mvn test`
   - Use "Skip tests" option if needed

2. **Authentication errors**:
   - Verify `GITHUB_TOKEN` permissions in repository settings
   - Ensure workflow has `packages: write` permission

3. **Version conflicts**:
   - Make sure version doesn't already exist
   - Use SNAPSHOT for development versions

4. **Local act testing fails**:
   - Verify Docker is running
   - Check `.secrets.act` file format
   - Try with `--pull` flag in `.actrc`

### Getting Help:

- **GitHub Actions logs**: Check the workflow execution logs
- **Package repository**: https://github.com/Clickin/egovframe-runtime/packages
- **act documentation**: https://nektosact.com/

## 📝 Next Steps

1. **Test the workflow**: Run `act-test.bat` locally
2. **Create PR**: Include all the new files
3. **Merge to main**: After PR approval
4. **First publish**: Use the workflow to publish first version
5. **Update documentation**: Add package consumption examples to main README

---

*This setup provides a complete, production-ready Maven publishing pipeline to GitHub Packages with local testing capabilities.*