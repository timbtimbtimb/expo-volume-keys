package expo.modules.volumekeys

import android.content.Context
import android.view.KeyEvent
import expo.modules.core.interfaces.Package
import expo.modules.core.interfaces.ReactActivityHandler

/**
 * Expo's `ReactActivityDelegateWrapper` offers every key the activity did not
 * dispatch to a view to these handlers before React Native sees it, and a
 * handler returning true consumes it. Consumed in the activity, a volume key
 * never reaches `PhoneWindow`, which is what hands it to the system to change
 * the volume — so this needs no change to `MainActivity`.
 */
class ExpoVolumeKeysPackage : Package {
  override fun createReactActivityHandlers(activityContext: Context?): List<ReactActivityHandler> =
    listOf(
      object : ReactActivityHandler {
        override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
          val listener = VolumeKeys.listener ?: return false
          val key = VolumeKeys.keyName(keyCode) ?: return false
          listener.onVolumeKey(key, (event?.repeatCount ?: 0) > 0)
          return true
        }

        // The release is consumed too: one left unconsumed still reaches
        // `PhoneWindow`, which hands volume keys to the system.
        override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean =
          VolumeKeys.listener != null && VolumeKeys.keyName(keyCode) != null
      },
    )
}
