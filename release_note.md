# Release Note: Version 1.9.4

Release Date: November 12, 2025

- **Added numeracy assessment results data models, including support for count and match, number operations, word problems, and number recognition.**
  - Introduced comprehensive data classes for numeracy assessments, enabling structured storage and retrieval of results for various numeracy activities. This supports better analytics and reporting for student performance.

- **Improved audio player composable for network audio playback, including playback state detection and resource cleanup.**
  - Enhanced the audio player to stream audio from network sources, detect when playback is finished, and automatically release resources. This prevents memory leaks and improves user experience during audio playback.

- **General stability and performance improvements.**
  - Applied multiple optimizations and bug fixes across the codebase, including improved error handling, faster data fetching, and more responsive UI components. The app is now more robust and reliable for daily use.

- **Localization fixes and translation updates.**
  - Fixed several missing or incorrect strings across multiple locales, improved text wrapping, and addressed right-to-left layout issues in select screens.

- **Accessibility improvements.**
  - Improved screen reader labels, focus order, and contrast ratios for a more accessible experience for users relying on assistive technologies.

- **Fixed a rare crash in the audio player while streaming.**
  - Resolved a race condition that could cause a crash during buffering/stop actions by adding defensive null checks and tightening lifecycle handling for the player.
