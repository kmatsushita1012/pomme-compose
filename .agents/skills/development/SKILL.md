---
name: development
description: Pomme Compose の要件確認、詳細設計、実装、Compose Preview、テスト、レビューに適用する開発ルール。
---

# Pomme Compose 開発スキル

Pomme Compose の開発作業で必ず適用する。利用者向けの資料ではない。

## 1. 作業前

1. ルートの AGENTS.md、docs/requirements.md、docs/architecture.md、docs/components.md、docs/api-design.md、docs/development-workflow.md を読む。
2. 作業対象の Issue、関連コンポーネント、承認済みの詳細設計・既存実装を確認する。
3. 要件と具体的な公開 IF を区別する。未確定の IF（命名、引数、型、DSL、デフォルト挙動等）は詳細設計として提案し、合意前に確定・実装しない。
4. 要件にない操作や仕様を追加する必要がある場合は、実装範囲を独断で広げない。

## 2. 実装ルール

- MyKanban の既存ワークフローに従い、原則 1 コンポーネント 1 Issue・1 ファイル・1 PR。対象固有の型・Scope・Preview は同一ファイルに置く。共通基盤、共有処理、テスト等の例外は docs/development-workflow.md に従う。
- ライブラリの共通 UI/API は `commonMain`、共通テストは `commonTest` に置く。初期の Kotlin Multiplatform target は Android のみとし、Android 固有実装は `androidMain` に置く。Compose Preview は共通ソースのコンポーネントと同じファイルに定義する。
- Typography は PommeTypography の全11種類の SwiftUI 準拠 TextStyle を用い、PommeTheme.typography と Compose 標準 Text / LocalTextStyle / ProvideTextStyle 等を活用する。個別コンポーネントにフォントサイズを重複定義せず、明示 style と Android のフォント倍率を尊重する。
- 公開 API は事前合意した IF を実装し、公開が不要な宣言は internal / private とする。公開 API には KDoc を記載する。安定版リリース後の破壊的変更は原則禁止。外部依存追加は事前合意が必要。
- 実装担当は PR 作成と Preview 画像の添付まで担当する。依存する Issue は GitHub の Blocked by で管理し、未解決の依存を無視して着手しない。

- 対応する SwiftUI 公式 API を確認し、公開名称（型・enum 値・引数・スタイル・Modifier 相当を含む）、責務、既定挙動、利用コードを照合する。独自の同義語や Material 由来の別概念へ安易に置き換えない。差異が必要なら理由・利用側への影響を記録して合意する。
- docs/api-design.md の利用例の構造を維持する。未承認のシグネチャは確定しない。Toolbar は明示配置を優先し、意味による自動配置を追加しない。
- SwiftUI の View / 構造要素は Pomme 接頭辞、View Modifier 相当の画面設定・構造は Scope の lowerCamelCase 関数、個別装飾は Modifier 拡張とする。PommeScaffold 内では navigationTitle、content、toolbar を使用し、独立した PommeToolbar は追加しない。
- 公開 UI コンポーネントは例外なく Pomme 接頭辞で統一する。Compose 標準の Text、Row、Column、Box、Spacer 等に不要な独自ラッパーを作らない。
- Form / List / Section、Scaffold / Toolbar の詳細設計では Compose の receiver Scope を活用し、SwiftUI のように構造や配置意図を宣言できる型安全な DSL を優先する。具体的な IF は承認済み詳細設計に従う。
- PommeForm / PommeSection の直接の子を原則一行として扱う。PommeRow 等の専用公開ラッパーを必須化・追加しない。複数要素は標準 Row / Column / Box 等でまとめ、行の標準装飾は親と内部共通基盤へ集約する。
- Scope は構造・配置意図、CompositionLocal は表示文脈、Modifier は個別の外観・レイアウト、Composable は UI の表現に使用する。配置先別の同種コンポーネントや、Scope ごとの入力コンポーネントの重複定義を避ける。
- Scope に業務状態や処理を持たせない。再コンポーズによる二重登録、条件付き要素の更新漏れ、外側 Scope の誤用などを防ぐ。過剰な独自 DSL を作らず Compose 標準機能と整合させる。
- Jetpack Compose 標準 API を優先し、要件に適合する Material 3 の機能は再利用する。cupertino-compose には依存しない。
- 既存の標準機能を独自実装に置き換えるだけの変更を避ける。局所的な問題解決のために全体の設計規則を壊さない。
- ライブラリ内に利用側の業務処理、ViewModel、DI、永続化、ナビゲーションツリーを含めない。
- 状態ホイスティングを基本とし、業務上の値を内部に複製・保持しない。状態を保持する必要がある場合も一時的な UI 状態に限る。
- 共通の配色、余白、区切り線、レイアウト等は適切な共有実装へ集約する。特定コンポーネント内に全体共通の規則を隠さない。
- 文字サイズ、長いテキスト、タップ領域、フォーカス、IME、TalkBack、WindowInsets を考慮する。
- 要件にない後方互換レイヤーやラッパーを増やさず、変更で不要になったコードを残さない。

- グラスモーフィズムの初期対象は PommeButton のみ。Toolbar 内の Automatic を Glass に解決し、明示的な具体スタイルを尊重する。他のコンポーネントや Toolbar 背景への適用、汎用公開 API の追加は行わない。

## 3. Compose Preview（必須）

**新規 UI コンポーネントには必ず Android Studio の Compose Preview（@Preview）を用意する。既存コンポーネントを変更した場合は対応する Preview を必ず確認・更新する。**

- 各コンポーネントを単独で確認できる Preview を用意する。カタログや親画面の Preview だけで済ませない。
- Preview には本番と同じテーマ・コンポーネントを利用する。Preview 専用の見た目を再実装しない。
- 実装した全 Style について個別の Preview 関数を同じコンポーネントファイルに定義する。Style を持たないコンポーネントも Preview は必須。
- PommeTypography は11種類の TextStyle ごとに個別 Preview を同一ファイルに定義し、Light / Dark 両方の実画像（最低22確認単位）を PR 本文に添付する。基盤型が後続 PommeTheme に依存しないよう、未実装のテーマを先行して作成せず実際の TextStyle で描画する。
- 各 Style の Light / Dark 双方を実際に描画・撮影する。Automatic 等で外観が変わる文脈ごとにも両テーマを確認する。
- コンポーネントに応じて、通常、無効、選択済み・未選択、未入力・入力済み等の主要状態を確認可能にする。関連するスタイルやレイアウトの違いも対象とする。
- サンプルデータで自立して描画できるようにし、ネットワーク、認証、実際の利用アプリの ViewModel 等を必要としない。
- PommeButton の Glass は単独の Preview と Toolbar 内の Automatic の Preview を用意する。ライト・ダーク、明示スタイルによる上書き、無効状態、異なる背景での可読性を確認できるようにする。
- Preview のためだけに公開 IF を追加・変更しない。配置や共通化方式は詳細設計に従う。
- KMP screenshot test source set は現行 screenshot Gradle plugin でコンパイルされないため、実画像は `:sample` の Android screenshot test adapter から Pomme の公開 API を描画して生成する。adapter にコンポーネントの実装を複製しない。
- Preview が存在しない、対象の状態が確認できない、またはコンパイルできない状態で実装を完了扱いにしない。
- 各 Style × Light / Dark（必要な文脈を含む）の実際の Preview 画像を PR 本文に必ず添付し、Style とテーマを識別できるようにする。画像は当該 PR の実装から生成したものとし、モック・既存画像・ビルド成功・外部リンクのみで代替しない。
- 描画環境や画像添付手段がない場合は阻害事項を報告し、実装完了・レビュー通過・マージ可能とは扱わない。画像の不足は解消する必要がある。

## 4. テストと検証

- 変更に応じたユニットテスト、Compose UI テストを追加・更新する。既存のテスト構成を尊重し、同じ責務のテストを重複させない。
- 入力の変更通知、無効状態、操作の重複発火、テーマ、スクロール・Insets 等を必要に応じて検証する。
- 非 UI ロジックは行カバレッジ 90% 以上を目標とする。Preview をカバレッジの代替にしない。
- ビルド・テスト・Lint は設定済みのプロジェクト手順に従う。CI の成功をマージ条件とする。未実行や失敗を隠さない。
- Preview だけでテストを代替しない。サンプルアプリも必要に応じて更新する。
- 対象 iOS 標準 UI との外観・操作感を比較し、Android の事情で差異が必要なら理由と影響を明示する。

## 5. 完了・レビュー条件

- [ ] 対象要件および承認済み詳細設計に一致する。
- [ ] 未承認の公開 IF や UI 仕様の追加がない。
- [ ] 公開 UI コンポーネント・構造要素の Pomme 接頭辞と、Modifier 相当の Scope 関数・Modifier 拡張の lowerCamelCase の区別を維持している。
- [ ] 対応 SwiftUI API と細部の名称・責務・既定挙動・利用構造を照合し、差異が必要なら理由を記録している。
- [ ] Form / List / Section、Scaffold / Toolbar の Scope 設計は宣言的で、誤用・再コンポーズ・動的な更新を考慮している（対象変更時）。
- [ ] Form / Section で直接配置が可能で、専用の行ラッパーを要求せず、標準レイアウトと親の行装飾を分離している（対象変更時）。
- [ ] アプリ固有の処理や不要な依存を持ち込んでいない。
- [ ] 共通化・責務分離が適切で、不要コードが残っていない。
- [ ] 1 コンポーネント 1 Issue・1 ファイル・1 PR の原則を守り、必要な例外は明示している。
- [ ] 公開 API に KDoc があり、不要な公開宣言がない。
- [ ] 実装した全 Style に個別の Compose Preview が同じファイルにあり、主要状態・必要な文脈・Light / Dark 両方を確認できる。
- [ ] PR 本文に全 Style × Light / Dark（必要な文脈を含む）の当該実装の実レンダリング画像が添付され、閲覧できる。未添付・不足・描画未確認の場合は完了不可。
- [ ] 必要なテスト・ビルド・Lint を実施し、結果と未確認事項を明記する。マージ前に CI 成功を確認する。
- [ ] 必要な開発資料・利用者向け資料をそれぞれ更新する。

具体的なコーディング規約、公開 IF の設計ルール、配布手順等は、それらが正式に設計された段階で追記する。
