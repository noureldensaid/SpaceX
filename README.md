# 🚀 SpaceX Launches 

Android application built as a home task to demonstrate **modern Android development best practices** using **Jetpack Compose**, **MVI**, and **Clean Architecture**.

---

## ✨ Features
- Paginated list of SpaceX launches
- Launch details screen with navigation
- Cursor-based pagination (GraphQL)
- Proper loading, empty, and error states
- Localization-ready (strings externalized)
- Light & Dark mode support

---

## 🧱 Architecture
- **Clean Architecture**
  - `presentation` (MVI + Compose)
  - `domain` (UseCases, models)
  - `data` (GraphQL, repositories)
- **MVI** for predictable state management

---

## 🌐 Data Source
- GraphQL API  
  `https://apollo-fullstack-tutorial.herokuapp.com/graphql`

---

## 🧪 Testing
- Unit tests for UseCases
- Repository interaction verification
- Error handling & edge cases
- Coroutine cancellation tested

---

## ⚠️ Error Handling
Unified error model using `NetworkError` enum  
(e.g. no internet, 404, client/server errors).

---

## 🛠 Tech Stack
- Kotlin
- Jetpack Compose
- Apollo GraphQL
- Coroutines
- MockK, JUnit

---

## 📌 Notes
- Offline mode is out of scope
- Focus on architecture, stability, and code quality
- Release-ready build configuration

---

👨‍🚀 *Focused, testable, and production-oriented Android implementation.*
