import { NativeModule, requireOptionalNativeModule } from 'expo'

export type VolumeKey = 'up' | 'down'

export interface VolumeKeyEvent {
  key: VolumeKey
  /** True for the events Android repeats while the button is held down. */
  repeat: boolean
}

export interface VolumeKeySubscription {
  remove: () => void
}

// A type alias, not an interface: only an alias is assignable to the
// `Record<string, …>` that `NativeModule` constrains its events map to.
type VolumeKeysEvents = {
  onVolumeKey: (event: VolumeKeyEvent) => void
}

declare class ExpoVolumeKeysModule extends NativeModule<VolumeKeysEvents> {}

// Android only: the module is not linked on iOS, where an app cannot take the
// volume buttons from the system, nor on the web.
const ExpoVolumeKeys =
  requireOptionalNativeModule<ExpoVolumeKeysModule>('ExpoVolumeKeys')

/** Whether this platform lets an app take the volume buttons. */
export const isAvailable: boolean = ExpoVolumeKeys != null

/**
 * Calls `listener` on each press of a volume button.
 *
 * While at least one listener is subscribed, the buttons are taken from the
 * system and stop changing the volume; removing the last one gives them back.
 * Where `isAvailable` is false this does nothing.
 */
export function addVolumeKeyListener(
  listener: (event: VolumeKeyEvent) => void,
): VolumeKeySubscription {
  if (ExpoVolumeKeys == null) return { remove: () => {} }

  return ExpoVolumeKeys.addListener('onVolumeKey', listener)
}
