package expo.modules.volumekeys

import android.view.KeyEvent

internal fun interface VolumeKeyListener {
  fun onVolumeKey(key: String, repeat: Boolean)
}

/**
 * What the module and the activity handler share. They are created apart —
 * the module per React context, the handler per activity — so neither can
 * hold the other.
 */
internal object VolumeKeys {
  /** Set exactly while JS listens; null means the keys belong to the system. */
  @Volatile
  var listener: VolumeKeyListener? = null

  /** The name JS receives for a volume key, null for any other key. */
  fun keyName(keyCode: Int): String? = when (keyCode) {
    KeyEvent.KEYCODE_VOLUME_UP -> "up"
    KeyEvent.KEYCODE_VOLUME_DOWN -> "down"
    else -> null
  }
}
