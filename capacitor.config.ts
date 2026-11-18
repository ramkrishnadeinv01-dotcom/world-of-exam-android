import type { CapacitorConfig } from '@capacitor/cli';

const config: CapacitorConfig = {
  appId: 'in.worldofexam.app',
  appName: 'World of Exam',
  webDir: 'www',
  server: {
    // Replace with your exact HTTPS GitHub Pages/custom-domain URL before building the APK.
    url: 'https://ramkrishnadeinv01-dotcom.github.io/live-quiz-competition/',
    cleartext: false,
    androidScheme: 'https'
  },
  android: { allowMixedContent: false }
};
export default config;
