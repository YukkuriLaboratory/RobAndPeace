# Rob&Peace

ここは穏便に窃盗で...

> このMODはしう氏の2025年1月企画、「ぬすっとクラフト」のために作成されました
> [![動画](http://img.youtube.com/vi/WLg57g4m2ew/0.jpg)](https://youtube.com/playlist?list=PLAk_kz3mvfCvNfA75-HPtVnjHRvTKRkCj)


このMODは攻撃の代わりに盗みというシステムに置き換え、いくつかの補助アイテムを追加します。

## アイテム

- グローブ: ドロップ確率上昇
- マジックハンド: mob以外に対するリーチ上昇
- ピッキングツール: 保管庫を開ける
- ポータルフープ: 壁を通り抜ける
- スモーク: 敵対mobのターゲットを解除
- スパイダーウォーカー: 壁や天井を歩けるように

## 前提MOD

- [FabricAPI](https://modrinth.com/mod/fabric-api)
- [Cloth Config](https://modrinth.com/mod/cloth-config)
- [ModMenu](https://modrinth.com/mod/modmenu)
- [Fabric Language Kotlin](https://modrinth.com/mod/fabric-language-kotlin)
- [ImmersivePortals](https://modrinth.com/mod/immersiveportals)
- [Cardinal Components API](https://modrinth.com/mod/cardinal-components-api)

> End-Remasteredと合わせて使用する場合は[こちら](https://github.com/YukkuriLaboratory/Steal-Looting)

## Build

```shell
git submodule update --init
cd gravity-api
./gradlew publishToMavenLocal
cd ..
./gradlew build
```

## Credits

- below things are borrowed from [PortalGun](https://github.com/iPortalTeam/PortalGun) under the MIT license
    - some codes
    - in src/main/resources/assets/robandpeace/textures/entity
        - overlay_filled.png
        - overlay_frame.png
- Some codes borrowed from [Better-Mobility](https://github.com/peanutsponge/Better-Mobility) under the MIT license
