# Pomme Compose — 要件

本資料は開発者向け。現段階では要件と設計の方向性を定める。公開 UI コンポーネントの命名規則は確定済みとする。その他の公開 IF、クラス・関数のシグネチャ、型、DSL、引数、デフォルト値、拡張ポイントの詳細は後続の設計で決定する。

## 目的

- Android の Jetpack Compose で iOS 標準 UI に近い見た目と操作感の画面を構築できるようにする。
- cupertino-compose に依存せず、必要な共通 UI を自ら管理できるようにする。
- 初期利用先である MaTool_Kotlin の UI を iOS 側の仕様と揃えやすくする。ただし Pomme 自体は MaTool に依存しない。
- 各アプリ画面で色・余白・角丸・区切り線・共通レイアウトを繰り返し調整する必要を減らす。

## 対象範囲

- Android / Jetpack Compose。Kotlin Multiplatform や iOS での直接動作は初期対象外。
- テーマ、入力系、リスト系（List / Form / Section）、スクリーン系（Scaffold / Toolbar）を主要対象とする。
- ダイアログ、アイコン等は cupertino-compose の依存解消と汎用性に必要な範囲で対象を精査する。
- ライブラリ本体と独立したサンプルアプリを用意する。
- 対応コンポーネントの内訳は [コンポーネント要件](components.md) を参照する。

## デザインの基準

- SwiftUI の標準コンポーネントと Apple Human Interface Guidelines を参考にする。利用する iOS バージョンや画面状態は詳細設計時に明確にする。
- 見た目だけでなく、選択・入力・無効状態・タップ領域・スクロール等の基本挙動も揃える。
- Android のシステム戻る操作、IME、WindowInsets、フォーカス、TalkBack、フォント倍率等を尊重する。
- iOS の UI を参考にしつつ、Apple 固有の素材やフォント・アイコンを無断で転用しない。
- 各アプリの画面にボタン、説明、確認手順、操作制限などを勝手に追加する根拠として本ライブラリを利用しない。

## 宣言的な画面構築

- 見た目だけでなく、利用側のコードも SwiftUI のように構造と意図を簡潔に表現できること。対応する公開 API の名称、意味、構成、文脈による既定スタイルを維持し、独自の同義語を導入しない。
- [API 要件・利用イメージ](api-design.md) の命名方針と構造を遵守する。コード例は設計の基準であり、未確定の関数シグネチャを承認するものではない。
- 公開 UI コンポーネント名は一律 Pomme 接頭辞を付ける。テーマは PommeTheme、カラースキームは PommeColorScheme、Typography は PommeTypography とする。
- 特に PommeForm / PommeList / PommeSection と PommeScaffold の toolbar / navigationTitle は Compose の Scope を活用し、親子関係や配置の文脈に沿って宣言的に構築できるよう設計する。
- PommeForm / PommeSection では Pomme の入力・操作コンポーネントや標準 Compose の Text、Row、Column、Box 等を直接配置し、原則として各直接の子レイアウトを一行として扱えること。専用の PommeRow は公開対象外とする。
- 複数要素を一行にまとめる場合は標準のレイアウトコンポーネントを利用する。行の余白、最低高さ、区切り線等の標準装飾は親が管理し、個別調整が必要な場合は Modifier 拡張を優先して検討する。
- 呼び出し側がレイアウトの内部構造や装飾を手動で組み立て直す必要を減らす。標準 Compose のコンテンツを組み合わせる柔軟性も確保する。
- PommeScaffold の Scope 内に navigationTitle、content、toolbar を宣言し、toolbar 内に PommeToolbarItem を配置する構造を採用する。独立した PommeToolbar は提供しない。SwiftUI の View Modifier 相当の画面設定・構造宣言は lowerCamelCase の Scope 関数を用いる。
- 合意済みの名称・構造を除く詳細な DSL 構文、Scope 型の詳細、公開メソッドの引数・オーバーロード、コンテンツの登録方式、List の遅延描画と直接配置の両立方法は未確定とする。

## テーマ・カラースキーム

ライトモード・ダークモードに対応し、次の6つの意味を持つ色を提供する。役割名は UIKit / SwiftUI の意味に合わせ、Pomme の公開プロパティ名も併記する。

| 色の役割 | iOS の参照 | Pomme プロパティ | 用途 |
| --- | --- | --- |
| アクセントカラー（Blue） | systemBlue / tint | accentColor | ボタン、リンク、選択可能な操作要素 |
| 主テキスト | Color.primary / label | primary | 主となるテキスト |
| 副テキスト | Color.secondary / secondaryLabel | secondary | 補足となるテキスト |
| 通常背景 | systemBackground | background | Form 等ではない通常画面の背景 |
| グループ化された背景 | systemGroupedBackground | groupedBackground | Form・グループ化された List の全体背景 |
| セクション背景 | secondarySystemGroupedBackground | secondaryGroupedBackground | グループ化された Section の内部背景 |

以下の背景色は UIKit の `systemBackground`、`systemGroupedBackground`、`secondarySystemGroupedBackground` を Light / Dark で解決して確認した値を既定値として採用する。Light では `background` と `secondaryGroupedBackground` がどちらも白になる。3役割が常に異なる色になるとは限らない。

| 役割 | Light | Dark |
| --- | --- | --- |
| アクセント | #007AFF | #0A84FF |
| 通常背景 | #FFFFFF | #000000 |
| グループ背景 | #F2F2F7 | #000000 |
| セクション背景 | #FFFFFF | #1C1C1E |

主テキストと副テキストは次の不透明度を使う。

| 役割 | Light | Dark |
| --- | --- | --- |
| primary | #000000 / 85% | #FFFFFF / 85% |
| secondary | #000000 / 55% | #FFFFFF / 55% |

初期の公開カラースキームはこの6役割を提供する。区切り線、プレースホルダー、無効状態、エラー等に必要な内部色は標準機能を活用し、公開色の増設を当然としない。壁紙由来の Dynamic Color で意図した配色を上書きしない。Material 3 を利用する箇所もテーマと整合させる。

## Typography

- SwiftUI の Font.TextStyle の全11種類を PommeTypography のプロパティとして提供する。名称は largeTitle / title / title2 / title3 / headline / body / callout / subheadline / footnote / caption / caption2。Material 由来の名称や独自の同義語に置き換えない。
- iOS の標準 Dynamic Type サイズを基準とした fontSize / lineHeight を sp で定義する。Android のシステムフォントとフォント倍率に追従し、Apple 固有フォントの再配布や iOS の Dynamic Type 段階そのものの独自再実装は行わない。
- Compose 標準 TextStyle と Text を利用する。PommeTheme.typography から参照でき、テーマで上書きできる構成とする。PommeText や独自フォント型・enum は設けない。
- コンポーネントの既定書体はテーマと表示文脈で解決する。利用側に毎回 fontSize を指定させない。具体的な対応・サイズ表は [API 要件・利用イメージ](api-design.md#typography) を参照する。
- PommeTypography は独立した1ファイル・1 Issue・1 PR とする。全11種類ごとに個別 Preview を定義し、Light / Dark 双方の実画像を PR 本文に添付する。

## グラスモーフィズム（初期範囲）

- Liquid Glass の完全な再現ではなく、半透明の表面・微細な輪郭・光沢・奥行きによる軽量なガラス風の外観を採用する。
- 初期の適用対象は PommeButton のみ。Toolbar 内ではスタイル未指定または Automatic の PommeButton に Glass を適用する。明示された具体的なスタイルは優先する。Toolbar 外の既定スタイルはこの要件によって変更しない。
- Toolbar 自体の背景、Form / List / Section、他の入力コンポーネント、汎用 glassEffect、複数のガラス要素の融合等は後続対応とする。
- ライト・ダーク両テーマ、背景による可読性、無効・押下状態、端末性能を考慮する。背景ぼかしの有無、描画手法、数値、API レベル別のフォールバックは詳細設計で決める。Liquid Glass と同等の光学表現は要求しない。

## 品質要件

- 全 UI コンポーネントに Style ごとの個別 Compose Preview を必須とする。各 Style（Style を持たない場合はコンポーネント）の Light / Dark 両テーマと該当する主要状態を実際に描画し、両テーマの画像を PR 本文に添付する。文脈によって変化する Automatic 等も対象とする。詳細は [開発ワークフロー](development-workflow.md) を参照する。
- サンプルアプリから実際の操作を確認できること。
- Compose の状態ホイスティングを尊重し、入力値や業務上の選択状態は呼び出し側を正本とする。
- 汎用的な UI の挙動をテストし、表示と状態変更の退行を防ぐ。Preview は自動テストの代替としない。
- 初期対応 Android バージョンは minSdk 26 とする。compileSdk とビルド基盤の構成は [構成・設計方針](architecture.md#初期ビルド構成) に従う。
- 初期の Gradle・Kotlin・Compose バージョンは基盤設計で確定済みの値を使う。依存バージョンは共有 Version Catalog で管理する。配布手順は後続設計で決める。

## 対象外

- アプリ固有の画面・データモデル・Presenter・ViewModel・DI・API 通信・永続化。
- ナビゲーションツリー、画面の遷移先・保存可否等の業務判断。
- SwiftUI 全 API の互換実装や、iOS のシステム全体の再現。
- この段階での合意済みの名称・構造以外の具体的な公開 IF の確定、ライブラリの実装、リリース。
