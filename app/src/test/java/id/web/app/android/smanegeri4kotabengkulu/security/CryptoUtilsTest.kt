package id.web.app.android.smanegeri4kotabengkulu.security

import org.junit.Assert.assertEquals
import org.junit.Test

class CryptoUtilsTest {

    @Test
    fun decryptsExamUrlFromApkConstant() {
        val decrypted = CryptoUtils.decrypt(
            "VBOUc8EYjrUI/45zlIvaow==:CsxPkXCyc5azaG2oU5bW/N4AlvI3fBT7MqWZ1d8sR8Y="
        )
        assertEquals("https://cbt-smanpa.sch.id", decrypted)
    }

    @Test
    fun decryptsUserAgentSuffixFromApkConstant() {
        val decrypted = CryptoUtils.decrypt(
            "cSIBhkPd+ijjFdE6Jr1aqA==:hL1viNGE3BzugL+8WQvjZa95FZorjAn2SzkP9TKaRSs="
        )
        assertEquals("dkg-cbt-sekolahkita.co.id", decrypted)
    }

    @Test
    fun computesLowercaseSha256Hex() {
        val digest = CryptoUtils.sha256Hex("abc".toByteArray(Charsets.UTF_8))
        assertEquals(
            "ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad",
            digest
        )
    }
}
