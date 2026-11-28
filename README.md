# Pomme Compose

Pomme Compose は、Kotlin Multiplatform / Compose Multiplatform を基盤とし、iOS の標準 UI に着想を得たコンポーネントを提供する UI ライブラリです。現在の対応ターゲットは Android のみです。

## 特徴（開発予定を含む）

- Light / Dark の標準色を提供する `PommeColorScheme`
- SwiftUI `Font.TextStyle` に対応する11種類の `PommeTypography`
- iOS の標準 UI を参考にした、統一感のあるシンプルな外観と操作感
- 入力コントロール、フォーム、リスト、セクション、画面レイアウトなどの共通コンポーネント
- ライトモード・ダークモードに対応するテーマ
- Jetpack Compose を利用する Android アプリへの組み込み

特定のアプリの業務ロジックや画面遷移方式には依存しません。

## 開発

### 必要な環境

- JDK 21
- Android SDK Platform 36
- Android Emulator（instrumentation test の実行時）

Gradle Wrapper は `JAVA_HOME` で指定した JDK を使います。`JAVA_HOME` と Android Studio の Gradle JDK を 21 に設定してください。

### ビルドとテスト

```shell
./gradlew assembleDebug
./gradlew testDebugUnitTest
./gradlew :sample:connectedDebugAndroidTest
```

`:sample:connectedDebugAndroidTest` は起動済みの Android Emulator または接続済みの Android 端末が必要です。Android Studio ではルートの `settings.gradle.kts` を開いてください。ローカル SDK の場所は `local.properties` に設定します。このファイルは Git 管理対象外です。

Gradle Wrapper、AGP、Kotlin、Compose、SDK の初期バージョンは [構成・設計方針](docs/architecture.md#初期ビルド構成) に記載しています。

## 開発状況

Kotlin Multiplatform / Compose Multiplatform の `:pomme` と、Android サンプルアプリ `:sample` を用意しています。`PommeColorScheme` と `PommeTypography` を実装済みです。利用可能なリリースはまだありません。

Pomme Compose は独立したプロジェクトであり、Apple の公式製品ではありません。
