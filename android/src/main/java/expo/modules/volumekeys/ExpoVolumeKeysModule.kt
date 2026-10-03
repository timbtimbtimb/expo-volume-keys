package expo.modules.volumekeys

import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition

private const val VOLUME_KEY_EVENT = "onVolumeKey"

class ExpoVolumeKeysModule : Module() {
  private val listener = VolumeKeyListener { key, repeat ->
    sendEvent(VOLUME_KEY_EVENT, mapOf("key" to key, "repeat" to repeat))
  }

  override fun definition() = ModuleDefinition {
    Name("ExpoVolumeKeys")

    Events(VOLUME_KEY_EVENT)

    // Subscribing is the switch: the keys are taken from the system exactly
    // while at least one JS listener exists, so there is no separate flag
    // for JS to forget to turn off.
    OnStartObserving(VOLUME_KEY_EVENT) {
      VolumeKeys.listener = listener
    }

    OnStopObserving(VOLUME_KEY_EVENT) {
      release()
    }

    // On a reload the next module can subscribe before this one is
    // destroyed, so the listener is only cleared while it is still this
    // module's.
    OnDestroy {
      release()
    }
  }

  private fun release() {
    if (VolumeKeys.listener === listener) {
      VolumeKeys.listener = null
    }
  }
}
