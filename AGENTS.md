# AGENTS.md

## 対象

本ファイルは Pomme Compose の開発者・開発エージェント向けの指示であり、ライブラリ利用者向けの説明書ではない。

## 作業前に必ず読む資料

- [開発スキル](.agents/skills/development/SKILL.md)
- [要件](docs/requirements.md)
- [構成・設計方針](docs/architecture.md)
- [コンポーネント要件](docs/components.md)

個別の作業では、対象 Issue と承認済みの詳細設計資料も確認する。

## 基本原則

- Android 向け Jetpack Compose の汎用 UI ライブラリとして開発する。
- iOS の標準 UI をデザイン・操作感の基準とし、Android の標準的な入力・アクセシビリティ・システム操作と両立させる。
- Compose の標準機能を優先し、cupertino-compose には依存しない。
- 特定アプリの業務ロジック、状態管理基盤、ルーティングを持ち込まない。
- Pomme が独自に提供する公開 UI コンポーネントは例外なく Pomme 接頭辞で統一する。Compose 標準の Text、Row、Column、Box、Spacer 等はそのまま利用し、不要なラッパーを作らない。
- Form / List / Section および Scaffold / Toolbar は SwiftUI に近い宣言的な構成を目指し、Compose の receiver Scope を活用した型安全な DSL を優先して設計する。具体的な Scope や DSL の IF は詳細設計で決める。
- PommeForm / PommeSection では直接の子コンテンツを原則として行として扱う。行専用の公開コンポーネント PommeRow は提供せず、複数要素を一行にまとめる場合は標準の Row / Column / Box 等を利用する。
- Scope は構造・配置意図、CompositionLocal は表示文脈、Modifier は個別の外観・レイアウト、Composable は実際の UI を担う。行の標準装飾は親と内部共通基盤が管理する。
- 要件と詳細設計を区別する。未承認の公開インターフェース（以下 IF）や仕様を実装者の判断で確定・追加しない。
- UI コンポーネントの新規実装・変更時は、対象コンポーネントの Compose Preview を必ず用意・更新する。サンプルアプリやテストだけで代替してはならない。
- 開発ルールの詳細と完了条件は開発スキルを参照する。

## 資料の役割

README.md は利用者向け。docs/ は開発者向けの要件・設計資料。.agents/skills/ は作業手順と開発規則を管理する。設計上の決定を README に先行して記載しない。
