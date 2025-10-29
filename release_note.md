# Release Note: Version 1.9.3

- **Added numeracy assessment results data models, including support for count and match, number operations, word problems, and number recognition.**
  - Introduced comprehensive data classes for numeracy assessments, enabling structured storage and retrieval of results for various numeracy activities. This supports better analytics and reporting for student performance.

- **Improved audio player composable for network audio playback, including playback state detection and resource cleanup.**
  - Enhanced the audio player to stream audio from network sources, detect when playback is finished, and automatically release resources. This prevents memory leaks and improves user experience during audio playback.

- **General stability and performance improvements.**
  - Applied multiple optimizations and bug fixes across the codebase, including improved error handling, faster data fetching, and more responsive UI components. The app is now more robust and reliable for daily use.


# Release Note: Version 1.9.4

- **Offline persistence & reliable survey submission.**
  - Enabled Firestore offline persistence and improved handling for household survey submissions when the device is offline. Submissions are queued locally and synchronized automatically when connectivity is restored.
  - Added local tracking for pending syncs (isPendingSync) so the app can show which records are waiting to be uploaded and avoid duplicate submissions.

- **Track and sync offline updates.**
  - Background work (WorkManager) is used to enqueue updates that should run when connectivity is available. Local updates are preserved and merged when the remote write completes.
  - Survey submission now validates local school info before attempting remote writes and falls back to queuing the submission when offline.

- **ViewModel & state improvements.**
  - Improved state flows to trigger functions when state changes (example: fetchHouseholds and survey submission workflows). Filtering logic for student lists now respects local assigned students and linked status (include student if isLinked == true or present in local assignments).

- **Crash fix: background learner detail updates.**
  - Fixed a crash occurring when updating learner details in a background worker (related to coroutine continuation / LiveEdit instantiation). The update path was hardened to use safe batched updates and preserve existing student fields when updating assigned students.

- **Miscellaneous stability fixes and improvements.**
  - Several small bug fixes and performance tweaks across the app to improve reliability and user experience.


<!-- End of release notes -->
