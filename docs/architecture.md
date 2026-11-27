# リポジトリ構成・設計方針

開発者向けの上位設計方針。以下は初期構成の方向性であり、実装の詳細や公開 IF を確定するものではない。

## リポジトリの構成

```text
pomme-compose/
├── README.md
├── AGENTS.md
├── docs/
├── .agents/skills/development/SKILL.md
├── pomme/                   # Android ライブラリ本体（予定）
│   └── src/
│       ├── main/
│       ├── test/
│       └── androidTest/
├── sample/                  # 動作確認・カタログアプリ（予定）
│   └── src/
├── gradle/                  # 共通ビルド設定（予定）
└── .github/workflows/       # CI（予定）
```

ライブラリ本体はテーマ、入力、リスト、スクリーン、必要なモーダル表示・アイコン等の責務で整理する。具体的なパッケージ構成と公開・非公開の境界は詳細設計で確定する。初期の Gradle モジュール分割はライブラリ本体とサンプルアプリを基本とし、コンポーネント単位での細分化を目的としない。

## 依存関係

- サンプルアプリ → Pomme ライブラリ → Jetpack Compose。
- ライブラリ本体はサンプルアプリや MaTool に依存しない。
- Compose Foundation、Runtime、UI 等を基本とし、Material 3 の既存コンポーネントを再利用できる場合は利用する。ただし外観・挙動が要件を満たすこと。
- cupertino-compose への依存は禁止。新しい外部 UI ライブラリの追加は理由を明示して設計上の合意を得る。
- Android の依存バージョン、Gradle プラグイン、配布方式等は基盤設計で決める。JitPack によるタグ配布は候補とし、決定事項ではない。

## UI の責務

- ライブラリ内部で共通の外観とレイアウトを管理する。色・寸法・状態表現の共通規則を画面ごとに再定義しない。
- 呼び出し側が業務上の状態と処理を所有する。コンポーネント内部で扱う状態はフォーカスやアニメーション等の一時的な UI 状態に限定する。
- List と Form は行の装飾・描画の共通基盤を共有する。行は親が扱う内部の描画単位であり、公開の行専用コンポーネントを前提としない。
- Scaffold は画面構造と Insets を担当し、ナビゲーションや保存処理を担当しない。
- 汎用処理は適切な共有位置に置き、単一画面やサンプル画面の局所実装として埋め込まない。
- UI の共通余白は 8dp・16dp を基準として検討し、iOS の標準 UI に合わせる必要がある寸法や Android のアクセシビリティ上の要件は詳細設計で整理する。

## 命名規則（確定）

- Pomme が独自に提供する公開 UI コンポーネントは一律 Pomme 接頭辞を付け、名前衝突の有無で例外を設けない。
- コンポーネント名は原則として SwiftUI の対応概念に合わせる。スクリーンの構成要素など直接対応しないものは責務に沿った名称とする。具体的な一覧は [コンポーネント要件](components.md) を参照する。
- テーマとカラースキームは PommeTheme / PommeColorScheme とする。
- Text、Row、Column、Box、Spacer 等の Compose 標準 API はそのまま利用する。Row / Column / Box はそれぞれ SwiftUI の HStack / VStack / ZStack に相当する汎用レイアウトとして利用する。PommeRow は公開対象外とし、独自のスタック代替も作らない。
- 公開型・enum 値・引数・スタイル・Modifier 相当の名称も SwiftUI の対応 API を優先する。例：PommeToolbarPlacement.TopBarLeading / TopBarTrailing。詳細は [API 要件・利用イメージ](api-design.md) を参照する。
- SwiftUI の View や ToolbarItem 等の構造要素には Pomme 接頭辞、View Modifier 相当の Scope 関数や Modifier 拡張には lowerCamelCase の対応名を使用する。例：PommeToolbarItem、toolbar、navigationTitle、listRowInsets。
- 上記は公開コンポーネントの規則であり、内部実装や DSL 内の補助メソッドまで一律に同じ接頭辞を付けることを要求しない。それらの名称は詳細設計で定める。

## Scope を活用した宣言的 UI の設計方針

SwiftUI のように、親の文脈の中で子の構造や配置意図を宣言できる API を目指す。Compose 標準の receiver Scope（RowScope、ColumnScope、LazyListScope 等）を参考に、必要に応じて型安全な DSL を設計する。

### Form / List / Section

- Form と Section では通常の Composable を直接配置し、各直接の子レイアウトを原則一行として扱う。PommeTextField、PommeToggle 等の独自コンポーネントだけでなく、標準 Compose の Text や任意の Composable も対象とする。専用の PommeRow によるラップを要求しない。
- 複数の要素を一行にまとめる場合は、標準の Row / Column / Box を利用する。行の内部構造は汎用レイアウトが担当し、Form / Section 専用のレイアウトを別途作らない。
- Form と List は行装飾の共通基盤を共有し、Section はヘッダー・フッターと行のまとまりを表現する。背景、角丸、余白、最低行高、区切り線等の標準装飾は親が管理する。
- 行単位の背景・区切り線・Insets 等の個別調整が必要な場合は、SwiftUI の listRowBackground / listRowInsets 等を参考に Modifier 拡張を優先して検討する。具体的な公開 IF は未確定。
- Scope は構造や配置意図を宣言する必要がある箇所で利用する。通常の子 Composable を Scope の専用メソッドとして重複定義しない。必要に応じて DSL marker 等による誤用防止を検討する。
- 静的な行、動的なデータ集合、条件付き表示を自然に扱う。長い List の遅延描画と Form の入力・フォーカスを考慮し、直接配置との両立方法は詳細設計で決める。
- Compose では Composable の呼び出し単位と実際に生成されるレイアウトノードは一致するとは限らない。ゼロ個・複数個のノードを生成するコンテンツの行としての扱い、状態の同一性、遅延描画の境界は詳細設計と検証の対象とする。任意の Composable を呼び出し単位で自動判別できるとは仮定しない。

### Scaffold / Toolbar

- PommeScaffold の Scope 内で navigationTitle(...)、content { ... }、toolbar { ... } を宣言する。toolbar は SwiftUI の .toolbar に対応する Scope 関数であり、独立した公開 PommeToolbar は提供しない。PommeToolbarScope 内で PommeToolbarItem を明示配置する。
- SwiftUI の toolbar / ToolbarItem の考え方を参考に、各操作要素の配置先を明示できるようにする。PommeToolbarPlacement の初期値域は TopBarLeading / TopBarTrailing / Principal / BottomBar。Principal は固定の中央領域として扱う。操作の意味による自動配置は初期対象外。利用側が毎回 Row / Box で配置や余白を組み直すことを前提にしない。
- Toolbar の構成宣言と Scaffold のレイアウト責務を分離する。Scope は構造を収集し、実際の描画内容は Composable ラムダとして扱う。具体的な実装方式は詳細設計で検証する。特定の画面遷移方式や保存・キャンセル操作を Scope に組み込まない。
- 一般的な画面だけでなく、地図等の任意のコンテンツを組み合わせられること。

### 共通の設計上の注意

| Compose の仕組み | 責務 |
| --- | --- |
| Scope | 親子構造・配置意図と画面全体の設定の宣言（toolbar、navigationTitle 等） |
| CompositionLocal | 親から子への表示文脈の伝達（Form 内、Toolbar 内等のスタイル適応） |
| Modifier | 各要素の外観・レイアウトの個別調整 |
| Composable | 実際の UI の表現 |

同じ入力・操作コンポーネントは配置先に応じた標準スタイルを適用できる設計を目指し、Form 用・Toolbar 用の同種コンポーネントを別々に増やさない。CompositionLocal に業務状態を持ち込まない。

初期のグラスモーフィズムは PommeButton に限定する。Toolbar の表示文脈を CompositionLocal 等で伝え、PommeButton の Automatic を Glass に解決する。ToolbarItem 側でボタンを二重に装飾せず、ボタン自身が外観とインタラクションを管理する。明示的なスタイルを上書きしない。Glass の描画は内部に閉じ、将来の共通化を妨げない構造とするが、現段階で他コンポーネントや汎用公開 glassEffect を追加しない。

- Scope はコンテンツの構成・配置の文脈を表現するために利用する。業務状態の保有やグローバルな登録機構に転用しない。
- 再コンポーズによる二重登録・古いコンテンツの残留、条件分岐や動的要素の更新漏れ、状態や要素の同一性の破壊を防ぐ。
- Compose の標準的な状態管理・描画方式を尊重する。型安全性のためだけに過剰な DSL や不要な階層を増やさない。
- 合意済みの Scope 関数名・宣言構造・初期配置名称を維持し、receiver 型の詳細、関数シグネチャ、項目の登録・描画方式、Toolbar の具体的なレイアウト・配置解決方式は詳細設計で決める。

## サンプルアプリ

コンポーネントカタログとして、各コンポーネント、主要な状態、テーマ切替、複数コンポーネントを組み合わせた Form や Scaffold を確認可能にする。サンプルのための状態はサンプル側に保持する。カタログは個々の Compose Preview を置き換えない。

## 未決定事項

確定済みの公開名称・宣言構造を除く IF の命名・型・シグネチャ・DSL、テーマ提供方式、List の遅延描画と直接配置の両立方法、複数レイアウトノードを生成するコンテンツの扱い、行単位の Modifier 拡張、具体的なレイアウト値、Android 対応バージョン、ビルド・CI・配布設定は後続設計の対象。仕様不足を実装者の独断で埋めず、要件と分けて明文化・合意する。
