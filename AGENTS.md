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
- 要件と詳細設計を区別する。未承認の公開インターフェース（以下 IF）や仕様を実装者の判断で確定・追加しない。
- UI コンポーネントの新規実装・変更時は、対象コンポーネントの Compose Preview を必ず用意・更新する。サンプルアプリやテストだけで代替してはならない。
- 開発ルールの詳細と完了条件は開発スキルを参照する。

## 資料の役割

README.md は利用者向け。docs/ は開発者向けの要件・設計資料。.agents/skills/ は作業手順と開発規則を管理する。設計上の決定を README に先行して記載しない。
