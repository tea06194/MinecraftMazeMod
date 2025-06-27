MinecraftMazeMod

Mod to build some mazes in Minecraft.

// TODO:
- Add documentation for  instruments, build, run, main functions. This documentation should be undersandable by Language Models.
- Add documentation for used libraries and their targets and relationships between them in graph visualisation and as readme.md section.

v.1.0.0
<img src="https://github.com/user-attachments/assets/41e6d679-749c-4b18-838b-005f0824254f" alt="v1.0.0" width="500" height="auto">

v1.0.1
<img src="https://github.com/user-attachments/assets/654afd8e-f74b-4334-85c0-64d1a381318b" alt="v1.0.1" width="auto" height="350">

v1.1.0
<img src="https://github.com/user-attachments/assets/bea16b41-e689-45e4-9a4d-095096369062" alt="v1.1.0" width="auto" height="350">

v1.1.1
<img src="https://github.com/user-attachments/assets/0879355f-755f-4761-8d7d-1caa0d634989" alt="v1.1.1" width="auto" height="350">

------------------------------------------------------------------------------------------------
Requirements:
 - JDK 21

`./gradlew clean`
`./gradlew runClient`

`./gradlew tasks` - for only lsp



------------------------------------------------------------------------------------------------

Source installation information for modders
-------------------------------------------
This code follows the Minecraft Forge installation methodology. It will apply
some small patches to the vanilla MCP source code, giving you and it access 
to some of the data and functions you need to build a successful mod.

Note also that the patches are built against "un-renamed" MCP source code (aka
SRG Names) - this means that you will not be able to read them directly against
normal code.

Setup Process:
==============================

Step 1: Open your command-line and browse to the folder where you extracted the zip file.

Step 2: You're left with a choice.
If you prefer to use Eclipse:
1. Run the following command: `./gradlew genEclipseRuns`
2. Open Eclipse, Import > Existing Gradle Project > Select Folder 
   or run `gradlew eclipse` to generate the project.

If you prefer to use IntelliJ:
1. Open IDEA, and import project.
2. Select your build.gradle file and have it import.
3. Run the following command: `./gradlew genIntellijRuns`
4. Refresh the Gradle Project in IDEA if required.

If at any point you are missing libraries in your IDE, or you've run into problems you can 
run `gradlew --refresh-dependencies` to refresh the local cache. `gradlew clean` to reset everything 
{this does not affect your code} and then start the process again.

Mapping Names:
=============================
By default, the MDK is configured to use the official mapping names from Mojang for methods and fields 
in the Minecraft codebase. These names are covered by a specific license. All modders should be aware of this
license, if you do not agree with it you can change your mapping names to other crowdsourced names in your 
build.gradle. For the latest license text, refer to the mapping file itself, or the reference copy here:
https://github.com/MinecraftForge/MCPConfig/blob/master/Mojang.md

Additional Resources: 
=========================
Community Documentation: https://docs.minecraftforge.net/en/1.19.2/gettingstarted/
LexManos' Install Video: https://youtu.be/8VEdtQLuLO0
Forge Forums: https://forums.minecraftforge.net/
Forge Discord: https://discord.minecraftforge.net/
