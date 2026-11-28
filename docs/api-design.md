# SwiftUI 準拠の API 要件・利用イメージ

開発者向け。SwiftUI の外観だけでなく、利用側の宣言的な構造、語彙、責務、文脈に応じた挙動を設計の基準とする。

本資料の命名規則・必須の挙動・初期範囲は遵守する。合意済みの名称・宣言構造は維持する。コード例は具体的な利用イメージであり、それ以外の最終的なシグネチャ、Scope 型の詳細、オーバーロード、内部実装を確定するものではない。対応候補は実装の必須要件に含めない。

## 1. 公開 API の命名原則

- SwiftUI の対応 API の公式名称を参照する。コンポーネント名だけでなく、型名、enum の値、引数、スタイル、Modifier 相当の操作まで対応を維持する。
- 独自の公開 UI コンポーネントと対応する独自公開型は Pomme 接頭辞を付ける。Kotlin の型・enum は PascalCase、関数・引数は lowerCamelCase。既存の確定名は維持する。
- SwiftUI の View / ToolbarItem 等の構造要素と View Modifier 相当の API を区別する。前者は Pomme 接頭辞、後者は lowerCamelCase の対応名を用いる。
- 画面全体の構造・設定を宣言する View Modifier 相当の API は Scope 関数を優先する。個別要素の装飾は Modifier 拡張を優先し、親から子への表示文脈の伝達は CompositionLocal を用いる。すべての View Modifier を Scope に移すわけではない。

| SwiftUI | Pomme | 分類 |
| --- | --- | --- |
| Form / Section | PommeForm / PommeSection | UI コンポーネント |
| ToolbarItem | PommeToolbarItem | Scope 内の構造要素 |
| .toolbar | toolbar | Scope 関数 |
| .navigationTitle | navigationTitle | Scope 関数 |
| .navigationBarTitleDisplayMode | navigationBarTitleDisplayMode（採用候補） | Scope 関数 |
| .listRowInsets / .listRowBackground | Modifier.listRowInsets / Modifier.listRowBackground（採用候補） | Modifier 拡張 |

- SwiftUI の topBarLeading を Leading / Left / Start、borderedProminent を Filled / Primary といった独自の同義語へ変えない。
- SwiftUI で区別される style（外観）、role（操作の意味）、placement（配置）、selection（選択値）、label（ラベル）、prompt（入力案内）は混同しない。
- Compose 標準 Text / Row / Column / Box / Spacer / Icon、Modifier、状態ホイスティングをそのまま活用する。SwiftUI の Binding や ViewBuilder の仕組み自体を再実装する必要はない。Binding は値と変更コールバックに対応させる。
- 対応する SwiftUI API がない場合のみ、責務に即した名称を採用する。PommeScaffold は Compose の画面ホストであり NavigationStack の代替ではない。
- SwiftUI の API をすべて実装することは目標ではない。必要な機能を選び、採用した概念の命名・挙動を揃える。
- SwiftUI との差異が避けられない場合は、対応する公式 API、技術的な理由、利用側への影響、代替案を設計資料に記載し、合意する。

## 2. Scaffold / Toolbar

参照：[ToolbarItemPlacement](https://developer.apple.com/documentation/swiftui/toolbaritemplacement)、[principal](https://developer.apple.com/documentation/swiftui/toolbaritemplacement/principal)、[ToolbarPlacement](https://developer.apple.com/documentation/swiftui/toolbarplacement)。

### 配置（初期範囲・名称確定）

| SwiftUI の対応名 | PommeToolbarPlacement | Pomme での配置 |
| --- | --- | --- |
| topBarLeading | TopBarLeading | 上部バーの論理的開始側 |
| topBarTrailing | TopBarTrailing | 上部バーの論理的終了側 |
| principal | Principal | 上部バーの中央領域 |
| bottomBar | BottomBar | 下部ツールバー |

PommeToolbarPlacement は PommeToolbarItem の配置先を指定する enum とする。SwiftUI の ToolbarItemPlacement に対応する（SwiftUI の ToolbarPlacement はバー自体を指定する別概念）。Principal は Pomme では固定の中央領域として扱い、自動的な意味解決はしない。

automatic、confirmationAction、cancellationAction、destructiveAction、primaryAction、navigation 等の意味的配置、自動配置、キーボード用・他プラットフォーム用の配置は初期対象外。配置は利用者が明示する。追加は別途要件を合意した場合のみ。

### 利用イメージ

```kotlin
PommeScaffold {
    navigationTitle("基本情報")

    content {
        PommeForm {
            PommeSection {
                PommeTextField("名称", text = name, onTextChange = onNameChange)
                PommeToggle("公開する", isOn = published, onIsOnChange = onPublishedChange)
            }
        }
    }

    toolbar {
        PommeToolbarItem(TopBarLeading) {
            PommeButton("キャンセル", onClick = onCancel)
        }
        PommeToolbarItem(TopBarTrailing) {
            PommeButton("保存", onClick = onSave)
        }
    }
}
```

TopBarLeading / TopBarTrailing は PommeToolbarPlacement の enum 値を直接 import して使用する。完全修飾した PommeToolbarPlacement.TopBarLeading も利用できること。Kotlin の context-sensitive resolution による省略も利用側の選択肢だが、実験的コンパイラ機能をライブラリ利用の必須条件にしない。独自の enum 別名を増やさない。

### Scope の宣言イメージ

```kotlin
@PommeDsl
interface PommeScaffoldScope {
    fun navigationTitle(title: String)
    fun content(content: @Composable () -> Unit)
    fun toolbar(content: PommeToolbarScope.() -> Unit)
}

@PommeDsl
interface PommeToolbarScope {
    fun PommeToolbarItem(
        placement: PommeToolbarPlacement,
        content: @Composable () -> Unit
    )
}

@Composable
fun PommeScaffold(
    modifier: Modifier = Modifier,
    content: PommeScaffoldScope.() -> Unit
)
```

この宣言例は合意済みの名称・構造を示す。追加の引数・オーバーロード・Scope の具体実装は詳細設計で決める。

- Scaffold の Scope で navigationTitle、content、toolbar を宣言し、toolbar の Scope で配置ごとの PommeToolbarItem を宣言する。表示される操作は通常の Composable とする。
- toolbar は SwiftUI の .toolbar に対応する構造宣言であり、独立した公開 PommeToolbar は提供しない。navigationTitle は Scaffold の引数でなく Scope 関数で設定する。
- Scope による構造宣言と Composable の描画を分離する。宣言を評価するたびに必要な構造を構築し、再コンポーズによる重複登録・古い要素の残留を防ぐ。内部のデータ構造や描画方式は詳細設計で検証する。
- タイトルは navigationTitle に対応する概念。Principal のカスタムコンテンツとの優先関係は詳細設計で定義する。
- 同じ配置への複数アイテム、条件付き表示、Toolbar の省略、地図等の任意のコンテンツを扱う。
- 配置、タイトルの中央揃え、長いタイトル、RTL、Insets、システムバー、IME の整合は親が管理する。利用側に Row / Box で Toolbar を組み直させない。
- 戻る・保存・キャンセルボタンを自動生成しない。配置は業務操作や画面遷移を実行しない。
- navigationBarTitleDisplayMode（Automatic / Inline / Large）や ToolbarItemGroup は必要性を確認してから採用する。採用時の名称は SwiftUI に合わせる。

## 3. Form / List / Section

参照：[Form](https://developer.apple.com/documentation/swiftui/form)、[Section](https://developer.apple.com/documentation/swiftui/section)、[Lists](https://developer.apple.com/documentation/swiftui/lists)。

```kotlin
PommeForm {
    PommeSection(
        header = { Text("基本情報") },
        footer = { Text("公開情報を編集できます") }
    ) {
        PommeTextField("名称", text = name, onTextChange = onNameChange)
        PommeToggle("通知", isOn = notified, onIsOnChange = onNotifiedChange)

        Row {
            Text("バージョン")
            Spacer(Modifier.weight(1f))
            Text(version)
        }

        Column {
            Text("タイトル")
            Text("説明")
        }
    }

    if (canDelete) {
        PommeSection {
            PommeButton("削除", role = PommeButtonRole.Destructive, onClick = onDelete)
        }
    }
}
```

- Pomme の入力・操作コンポーネント、Compose 標準 Text、任意の Composable を直接配置できること。PommeRow や入力専用の行ラッパーは作らない。
- 直接の子レイアウトを原則一行とし、Row / Column / Box 内の要素はまとまった一行とする。Section 外の直接配置も自然に扱えるよう、暗黙のグループ化を対象 iOS に照らして設計する。
- Section の header / footer は任意とし、行と分けて描画する。行の高さは内容に追従し、背景、角丸、余白、最低行高、区切り線を親が管理する。空、単一行、複数行、条件分岐、長文・フォント倍率を検証する。
- Form と List で行装飾・Section の意味を共有する。Form の入力フォーカスや IME が再コンポーズで失われないこと。

### スタイルと個別調整

| SwiftUI の語彙 | 対応名称・意味 |
| --- | --- |
| listStyle(.plain) | PommeListStyle.Plain（初期必須） |
| listStyle(.insetGrouped) | PommeListStyle.InsetGrouped（初期必須） |
| listStyle(.automatic / .grouped) | PommeListStyle.Automatic / Grouped（候補） |
| formStyle(.automatic / .grouped) | PommeFormStyle.Automatic / Grouped（明示指定は候補、標準 Form は grouped 相当） |
| listRowInsets | 行の内側余白（候補） |
| listRowBackground | 行背景（候補） |
| listRowSeparator | 区切り線の表示（候補） |
| listRowSeparatorTint | 区切り線の色（候補） |

行単位の調整は Modifier 拡張を優先して検討し、採用する場合は listRowInsets 等の対応名を用いる。通常の padding / background とは異なり、親に行装飾の指定を伝えるものとして設計する。具体的な伝達方法と適用範囲は検証が必要。

### 動的 List の利用イメージ

```kotlin
PommeList(style = PommeListStyle.InsetGrouped) {
    PommeSection(header = { Text("町一覧") }) {
        items(districts, key = { it.id }) { district ->
            PommeButton(district.name, onClick = { onSelect(district.id) })
        }
    }
}
```

items は Compose LazyListScope に沿った暫定案であり、SwiftUI ForEach に対応する構造。安定キーを用いる。静的な直接配置との共存方法、ゼロ個・複数個のレイアウトノードを生成する Composable の扱いは未確定。任意の Composable の呼び出しを自動で一行と認識できるとは仮定しない。長い List で各行を遅延描画することを目指し、Section 全体を一つの巨大な遅延項目にして済ませない。Form の直接配置を犠牲にしない。

## 4. 入力・操作

参照：[Button](https://developer.apple.com/documentation/swiftui/button)、[ButtonRole](https://developer.apple.com/documentation/swiftui/buttonrole)、[TextField](https://developer.apple.com/documentation/swiftui/textfield)、[PickerStyle](https://developer.apple.com/documentation/swiftui/pickerstyle)、[Toggle](https://developer.apple.com/documentation/swiftui/toggle)。

### Button

```kotlin
PommeButton("保存", onClick = onSave)
PommeButton("削除", role = PommeButtonRole.Destructive, onClick = onDelete)
PommeButton("ログイン", style = PommeButtonStyle.BorderedProminent,
    enabled = canLogin, onClick = onLogin)
PommeButton(onClick = onAdd) {
    Icon(Icons.Default.Add, contentDescription = "追加")
}
```

| SwiftUI | Pomme の対応名称（採用時） |
| --- | --- |
| ButtonStyle.automatic | PommeButtonStyle.Automatic |
| plain / borderless | PommeButtonStyle.Plain / Borderless |
| bordered / borderedProminent | PommeButtonStyle.Bordered / BorderedProminent |
| glass | PommeButtonStyle.Glass（初期必須） |
| glassProminent | PommeButtonStyle.GlassProminent（後続検討） |
| ButtonRole.destructive / cancel | PommeButtonRole.Destructive / Cancel |

Automatic は配置文脈に応じた外観で Plain の別名ではない。role は意味と表示であり、確認ダイアログ・画面遷移を自動実行しない。通常操作には role 未指定を基本として検討する。Form / Toolbar 用の別コンポーネントを作らない。スタイルの初期採用範囲は詳細設計で整理する。

### Glass スタイル（初期必須）

参照：[SwiftUI glass](https://developer.apple.com/documentation/swiftui/primitivebuttonstyle/glass)、[glassProminent](https://developer.apple.com/documentation/swiftui/primitivebuttonstyle/glassprominent)。

PommeButtonStyle.Glass は SwiftUI の glass に対応する名称だが、初期の描画は軽量なグラスモーフィズムとし、Liquid Glass 自体の再現を要求しない。

```kotlin
// 任意の画面で明示的に Glass を指定
PommeButton("操作", style = PommeButtonStyle.Glass, onClick = onAction)

// Toolbar 内では Automatic（未指定）を Glass に解決
PommeScaffold {
    navigationTitle("編集")
    content { /* ... */ }
    toolbar {
        PommeToolbarItem(TopBarLeading) {
            PommeButton("キャンセル", onClick = onCancel)
        }
        PommeToolbarItem(TopBarTrailing) {
            PommeButton("保存", onClick = onSave)
        }
    }
}
```

| 配置文脈 | style | 解決される外観 |
| --- | --- | --- |
| Toolbar 内 | 未指定 / Automatic | Glass |
| Toolbar 内 | 明示した具体スタイル | 指定されたスタイル |
| Toolbar 外 | Glass | Glass |
| Toolbar 外 | 未指定 / Automatic | 従来の文脈依存の既定スタイル |

- 半透明の表面、控えめな輪郭・光沢、奥行き感を目指す。形状はラベルの内容に適応し、文字・アイコンの視認性とタップ領域を維持する。色や形状の具体値は詳細設計で決定する。
- 実際の背景ぼかし、反射、屈折、複数要素の融合、GlassProminent、汎用 glassEffect は初期必須ではない。対応 Android バージョンに応じた描画・性能の検証とフォールバックは詳細設計で扱う。
- Toolbar の背景そのものや他の UI コンポーネントは今回のガラス化の対象外。Toolbar 専用 Button を新設せず、Button 自身のスタイル解決を利用する。
- Light / Dark、通常・無効、異なる背景、Toolbar 内の自動適用と明示スタイルの上書きを Compose Preview と必要なテストで確認する。

### 入力

```kotlin
PommeTextField("名称", text = name, onTextChange = onNameChange)
PommeSecureField("パスワード", text = password, onTextChange = onPasswordChange)
PommeTextEditor(text = description, onTextChange = onDescriptionChange)
PommeToggle("通知", isOn = notifications, onIsOnChange = onNotificationsChange)
PommeSlider(value = opacity, onValueChange = onOpacityChange, valueRange = 0f..1f)
```

| SwiftUI の概念 | Pomme の命名・意味（採用時） |
| --- | --- |
| TextField / SecureField / TextEditor の text | text / onTextChange |
| title / label / prompt | タイトル・ラベル・入力案内の区別 |
| textFieldStyle(.automatic / .plain / .roundedBorder) | PommeTextFieldStyle.Automatic / Plain / RoundedBorder |
| Toggle の isOn | isOn / onIsOnChange |
| toggleStyle(.automatic / .switch / .button) | PommeToggleStyle.Automatic / Switch / Button |
| Slider / Stepper の value | value / onValueChange。範囲・step を対応づける |
| DatePicker の selection / displayedComponents | selection / onSelectionChange、displayedComponents |
| datePickerStyle(.automatic / .compact / .graphical / .wheel) | PommeDatePickerStyle.Automatic / Compact / Graphical / Wheel |

名称の対応は設計基準とし、各スタイルの実装範囲・入力型・日付時刻型・変更イベント・ラベルの具体的な IF は詳細設計で決める。SwiftUI の文字列タイトルが必ず別ラベルとして表示されるとは仮定しない。状態は利用側を正本とする。

### Picker

```kotlin
PommePicker(
    "表示形式",
    selection = mode,
    onSelectionChange = onModeChange,
    style = PommePickerStyle.Segmented
) {
    // SwiftUI の tag と同様、選択値と表示内容を対応づける暫定宣言
    item(tag = Mode.Map) { Text("地図") }
    item(tag = Mode.List) { Text("一覧") }
}
```

- selection / tag は同じ型で扱えること。表示文言や配列インデックスを選択値の代用にしない。
- SwiftUI の pickerStyle(.automatic / .menu / .segmented / .wheel / .inline / .navigationLink) に対応する名称は PommePickerStyle.Automatic / Menu / Segmented / Wheel / Inline / NavigationLink。初期実装は必要な選択形式に絞り、NavigationLink 等はルーティングを含むため採否を別途判断する。
- tag の伝達方式・Scope の item 宣言・動的選択肢の構成は未確定。選択肢の追加・削除、ラベル、無効状態、重複 tag の扱いを設計する。

## 5. Theme / Modifier / 文脈

```kotlin
PommeTheme {
    // 通常画面、Form、Toolbar で同じ PommeButton を利用できる
}
```

- PommeTheme / PommeColorScheme / PommeTypography の名称は確定済み。PommeColorScheme は requirements.md の6役割（accentColor、primary、secondary、background、groupedBackground、secondaryGroupedBackground）を提供する。primary / secondary は Light で黒を85% / 55%、Dark で白を85% / 55%の不透明度で使用する。背景色プロパティ名と既定値も requirements.md の表に従う。
- SwiftUI の tint、disabled、buttonStyle、pickerStyle 等は見た目だけでなく適用範囲・優先順位を参照する。Compose の標準機能で同じ意味を表現できる場合はそれを使う。
- Scope は構造・配置、CompositionLocal は親から子への表示文脈、Modifier は個別要素の調整、Composable は実際の UI。Modifier が子孫への環境伝達を自動的に行うとは仮定しない。
- 明示的なスタイルは文脈による既定スタイルに優先する。無効状態・アクセシビリティを失わせない。スタイルを指定する公開手段は詳細設計で決める。

### Typography

参照：[Font.TextStyle](https://developer.apple.com/documentation/swiftui/font/textstyle)、[Apple HIG Typography](https://developer.apple.com/design/human-interface-guidelines/typography)。

SwiftUI の名称に合わせ、通常の Dynamic Type サイズを基準に以下の値を採用する。単位は Compose の sp。headline は Semibold、それ以外の基本ウェイトは Normal とし、コンポーネント固有のウェイト調整は必要に応じて行う。

| プロパティ | fontSize | lineHeight | fontWeight |
| --- | ---: | ---: | --- |
| largeTitle | 34.sp | 41.sp | Normal |
| title | 28.sp | 34.sp | Normal |
| title2 | 22.sp | 28.sp | Normal |
| title3 | 20.sp | 25.sp | Normal |
| headline | 17.sp | 22.sp | SemiBold |
| body | 17.sp | 22.sp | Normal |
| callout | 16.sp | 21.sp | Normal |
| subheadline | 15.sp | 20.sp | Normal |
| footnote | 13.sp | 18.sp | Normal |
| caption | 12.sp | 16.sp | Normal |
| caption2 | 11.sp | 13.sp | Normal |

UIKit の title1 / caption1 ではなく SwiftUI の title / caption を使用する。フォントは Android のシステムフォントを使用し、フォント倍率への追従を維持する。iOS 固有のフォントファイルは使用しない。

#### 公開 IF の利用イメージ

```kotlin
@Immutable
data class PommeTypography(
    val largeTitle: TextStyle = TextStyle(fontSize = 34.sp, lineHeight = 41.sp, fontWeight = FontWeight.Normal),
    val title: TextStyle = TextStyle(fontSize = 28.sp, lineHeight = 34.sp, fontWeight = FontWeight.Normal),
    val title2: TextStyle = TextStyle(fontSize = 22.sp, lineHeight = 28.sp, fontWeight = FontWeight.Normal),
    val title3: TextStyle = TextStyle(fontSize = 20.sp, lineHeight = 25.sp, fontWeight = FontWeight.Normal),
    val headline: TextStyle = TextStyle(fontSize = 17.sp, lineHeight = 22.sp, fontWeight = FontWeight.SemiBold),
    val body: TextStyle = TextStyle(fontSize = 17.sp, lineHeight = 22.sp, fontWeight = FontWeight.Normal),
    val callout: TextStyle = TextStyle(fontSize = 16.sp, lineHeight = 21.sp, fontWeight = FontWeight.Normal),
    val subheadline: TextStyle = TextStyle(fontSize = 15.sp, lineHeight = 20.sp, fontWeight = FontWeight.Normal),
    val footnote: TextStyle = TextStyle(fontSize = 13.sp, lineHeight = 18.sp, fontWeight = FontWeight.Normal),
    val caption: TextStyle = TextStyle(fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Normal),
    val caption2: TextStyle = TextStyle(fontSize = 11.sp, lineHeight = 13.sp, fontWeight = FontWeight.Normal)
)
```

`PommeTypography()` は上記の標準値を返す。任意の名前付き引数または data class の `copy` で個別の `TextStyle` を置き換え、置き換えた値は暗黙にマージ・補正しない。標準値で明示する属性は `fontSize`・`lineHeight`・`fontWeight` のみとし、他属性は Compose の `TextStyle` の未指定値に委ねる。`sp` を使用して Android のフォント倍率に追従する。Light / Dark で同じ標準値を使い、Typography 自体は色やテーマ状態を保持しない。`PommeTheme` は `PommeTypography()` を既定値として使用する。

```kotlin
val typography = PommeTypography()
Text("Sample title", style = typography.title)

val customized = PommeTypography(
    body = typography.body.copy(fontSize = 18.sp, lineHeight = 24.sp)
)
```

```kotlin
PommeTheme(
    colorScheme = colorScheme,
    typography = typography
) {
    Text("Sample title", style = PommeTheme.typography.title)
    Text("Sample body text", style = PommeTheme.typography.footnote)
    Text(
        "Another heading",
        style = PommeTheme.typography.title2,
        fontWeight = FontWeight.Bold
    )
}
```

PommeTheme.typography で参照し、Compose 標準の TextStyle / Text / FontWeight 等を使用する。PommeText や独自フォント型・enum は設けない。標準値を提供するコンストラクタ、標準値で明示する属性、および PommeTheme での既定値は上記のとおりとする。

#### 文脈による既定書体

通常の Text は body を基準とし、コンポーネントごとに SwiftUI の実際の外観と照合する。下記は初期の対応候補であり、個別コンポーネントの詳細仕様で確定する。

| 配置・用途 | 既定候補 |
| --- | --- |
| 通常 Text / Button / TextField / Toggle ラベル / Picker ラベル | body |
| Section header / footer | footnote |
| Toolbar title | headline |
| Large Navigation Title（採用時） | largeTitle |

LocalTextStyle / ProvideTextStyle 等を活用して子コンテンツへ文脈に応じた既定書体を伝える。呼び出し側が毎回サイズを指定する必要がないようにし、明示的な style・fontWeight 等の指定を尊重する。

#### Preview とタスク

PommeTypography.kt は独立した1ファイル・1 Issue・1 PR。全11種類それぞれの個別 Preview を定義し、Light / Dark 両方の画像を PR 本文に添付する（最低22確認単位）。PommeTheme は PommeColorScheme と PommeTypography の両方に依存する。先行する基盤の Preview は実際の基盤型の値を使用し、後続の PommeTheme への逆依存を作らない。

## 6. その他の対応名称（採用候補）

| SwiftUI の概念 | Pomme の対応 |
| --- | --- |
| alert | PommeAlert |
| confirmationDialog | PommeConfirmationDialog |
| sheet | PommeSheet |
| ProgressView | PommeProgressView |
| ButtonRole.cancel / destructive | 対応する共通の PommeButtonRole |

SwiftUI の alert / confirmationDialog / sheet はプレゼンテーションの修飾子である。Pomme では Compose の表示状態とコールバックを利用し、利用側のルーティングを奪わない。具体的な公開 IF と採否は詳細設計で判断する。

## 7. 受け入れ・詳細設計時の確認

- 対象 iOS バージョンと公式の対応 API を明記する。
- 公式名称、対応語彙、引数の意味、既定値、配置文脈、入力・操作挙動を照合する。
- 本資料の利用例の構造を維持する。各画面の手動装飾、専用行ラッパー、配置先別コンポーネントを追加して逃げない。
- 未決定の技術課題（直接配置と遅延描画、Modifier による親への情報伝達等）を小規模な検証で解消してから公開 IF を確定する。
- 全 UI コンポーネントに Style ごとの個別 Compose Preview を用意し、Light / Dark 両方、必要な主要状態、Form / Toolbar 等の配置文脈の差を実際に描画する。各 Style × Light / Dark の画像を PR 本文に必ず添付する。Style がない場合も両テーマ必須。詳細は [開発ワークフロー](development-workflow.md) を参照する。
