# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.3.0] - 2026-07-07

### Added
- `II18nService` – contract for an internationalization (i18n) service that resolves II18nKey instances to localized strings
- `II18nKey` – Defines an internationalization key used for message translation
- Automatic-Module-Name option for jar
- GetterMonad has been replaced by NsFunc

## [1.2.0] - 2025-03-09

### Added
- `EnumFlags<TEnum>` – a type‑safe, fluent wrapper over `EnumSet` for managing enum flag sets.  
  See usage examples in the [README](README.md#enumflagstenum-extends-enumtenum-).

### Deprecated
- `GetterMonad` – this class is no longer recommended. Use `java.util.Optional` or functional alternatives for null‑safe chaining. It will be removed in version `2.0.0`.

## [1.1.0] - 2025-09-24
### First Publication
- EventManager and RestorePointEventManager
- IdentityComparator
- Stream conversion utilities
- Exception analysis tools
- And so on
