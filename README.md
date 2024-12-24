# Rob&Peace

ここは穏便に窃盗で...

## 前提MOD

- [FabricAPI](https://modrinth.com/mod/fabric-api)
- [Cloth Config](https://modrinth.com/mod/cloth-config)
- [ModMenu](https://modrinth.com/mod/modmenu)
- [Fabric Language Kotlin](https://modrinth.com/mod/fabric-language-kotlin)
- [ImmersivePortals](https://modrinth.com/mod/immersiveportals)
- [Cardinal Components API](https://modrinth.com/mod/cardinal-components-api)

## Build
```shell
git submodule update --init
cd gravity-api
./gradlew publishToMavenLocal
cd ..
./gradlew build
```

## Credits
- Including [GravityChanger-1.21.1](https://github.com/FugLord77/GravityChanger-1.21.1)
- below things are borrowed from [PortalGun](https://github.com/iPortalTeam/PortalGun) under the MIT license
  - some codes
  - in src/main/resources/assets/robandpeace/textures/entity
    - overlay_filled.png
    - overlay_frame.png
- Some codes borrowed from [Better-Mobility](https://github.com/peanutsponge/Better-Mobility) under the MIT license