package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.ControlLayoutsRepository
import com.example.model.LayoutCategory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("500+ control layouts", appName)
  }

  @Test
  fun `verify total layouts count exceeds 500`() {
    val total = ControlLayoutsRepository.layouts.size
    assertTrue("Total layouts should be at least 500, but was $total", total >= 500)
  }

  @Test
  fun `verify every gamemode has at least 50 layouts`() {
    val categories = listOf(
      LayoutCategory.CRYSTAL,
      LayoutCategory.MACE,
      LayoutCategory.YOUTUBER,
      LayoutCategory.ANCHOR,
      LayoutCategory.CART,
      LayoutCategory.MACRO_SWIPE,
      LayoutCategory.SWORD_AXE,
      LayoutCategory.LEGACY_189
    )

    for (cat in categories) {
      val count = ControlLayoutsRepository.layouts.count { it.category == cat }
      assertTrue("Category ${cat.displayName} should have at least 50 layouts, but had $count", count >= 50)
    }
  }

  @Test
  fun `verify crystal_34 and Not Altino layouts exist`() {
    val crystal34 = ControlLayoutsRepository.layouts.find { it.fileName == "crystal_34.json" }
    assertNotNull("crystal_34.json should exist", crystal34)

    val notAltinoProfile = ControlLayoutsRepository.creatorProfiles.find { it.name == "Not Altino" }
    assertNotNull("Not Altino creator profile must exist", notAltinoProfile)
    assertEquals("@altino_b4b", notAltinoProfile?.handle)
    assertEquals("Not_Altino", notAltinoProfile?.discord)
  }

  @Test
  fun `verify joystick layouts count is at least 60`() {
    val joystickLayouts = ControlLayoutsRepository.layouts.filter { it.hasJoystick }
    assertTrue("Should have at least 60 joystick layouts, but had ${joystickLayouts.size}", joystickLayouts.size >= 60)

    val sampleJoystick = joystickLayouts.first()
    val joystickButton = sampleJoystick.previewButtons.find { it.isJoystick }
    assertNotNull("Joystick layout must contain a button with isJoystick=true", joystickButton)
    assertEquals(true, joystickButton?.isJoystick)
  }

  @Test
  fun `verify room database persists custom layout offline`() = kotlinx.coroutines.runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val db = androidx.room.Room.inMemoryDatabaseBuilder(
      context,
      com.example.data.local.AppDatabase::class.java
    ).allowMainThreadQueries().build()

    val customEntity = com.example.data.local.CustomLayoutEntity(
      id = "custom_test_1",
      name = "My Test Mobile Control",
      fileName = "custom_my_control.json",
      category = "CRYSTAL",
      description = "Offline custom control layout",
      jsonContent = "{\"version\":2,\"controlMap\":[]}",
      createdAt = System.currentTimeMillis()
    )

    db.layoutDao().insertCustomLayout(customEntity)
    val customLayouts = db.layoutDao().getAllCustomLayouts().first()
    assertEquals(1, customLayouts.size)
    assertEquals("My Test Mobile Control", customLayouts[0].name)
    assertEquals("custom_my_control.json", customLayouts[0].fileName)

    db.close()
  }

  @Test
  fun `verify sequential counting layout names like crystal_1_json crystal_2_json`() {
    val crystalLayouts = ControlLayoutsRepository.layouts.filter { it.category == LayoutCategory.CRYSTAL }
    for (i in 1..75) {
      val expectedName = "crystal_$i.json"
      val found = crystalLayouts.find { it.name == expectedName }
      assertNotNull("Layout with sequential name $expectedName must exist", found)
      assertEquals(expectedName, found?.fileName)
    }

    val maceLayouts = ControlLayoutsRepository.layouts.filter { it.category == LayoutCategory.MACE }
    for (i in 1..65) {
      val expectedName = "mace_$i.json"
      val found = maceLayouts.find { it.name == expectedName }
      assertNotNull("Layout with sequential name $expectedName must exist", found)
      assertEquals(expectedName, found?.fileName)
    }
  }
}

