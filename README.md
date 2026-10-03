# @snowmap.fr/expo-volume-keys

Use the hardware volume buttons as input in an Expo app on **Android**. While a
listener is subscribed the buttons are taken from the system: they stop
changing the volume and showing its slider, and each press reaches your
listener instead. Removing the last listener gives them back.

iOS gives an app no way to do this, so there the module is not linked and the
listener is never called — likewise on the web. Check `isAvailable` before
offering the feature.

## Install

```sh
npx expo install @snowmap.fr/expo-volume-keys
```

It is a native module, so it needs a development build (not Expo Go) and a
rebuild after installing. Nothing to add to `app.config` or `MainActivity`.

## Use

```ts
import { addVolumeKeyListener, isAvailable } from '@snowmap.fr/expo-volume-keys'

useEffect(() => {
  const subscription = addVolumeKeyListener(({ key, repeat }) => {
    if (repeat) return // held down: Android repeats the key
    console.log(key) // 'up' | 'down'
  })
  return () => {
    subscription.remove()
  }
}, [])
```

Subscribe only while the buttons mean something to the screen — for a screen
in a navigator, from `useFocusEffect` — so the user keeps their volume
everywhere else.

### API

- `isAvailable: boolean` — whether this platform lets an app take the volume
  buttons (Android only).
- `addVolumeKeyListener(listener): { remove() }` — `listener` receives
  `{ key: 'up' | 'down', repeat: boolean }` for each press, `repeat` being true
  for the events Android repeats while a button is held down.

## How it works

Expo's `ReactActivityDelegateWrapper` offers the activity's key events to every
module's `ReactActivityHandler` before React Native sees them. This module's
handler consumes the two volume keys while JS listens (Expo's
`OnStartObserving` / `OnStopObserving`). A key consumed in the activity never
reaches `PhoneWindow`, which is what passes it on to the system to change the
volume.

What follows from that:

- A key only reaches the activity's own window. While a dialog or React
  Native's `Modal` is open, the buttons change the volume as usual.
- Nothing is delivered while the app is in the background or the screen is off.

## Licence

MIT
