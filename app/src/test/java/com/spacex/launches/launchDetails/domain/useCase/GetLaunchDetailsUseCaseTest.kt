package com.spacex.launches.launchDetails.domain.useCase

import com.spacex.launches.core.data.remote.NetworkError
import com.spacex.launches.core.data.remote.ResponseState
import com.spacex.launches.launchDetails.domain.model.LaunchDetailsModel
import com.spacex.launches.launchDetails.domain.repository.LaunchDetailsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetLaunchDetailsUseCaseTest {

    private val repository: LaunchDetailsRepository = mockk()
    private val useCase = GetLaunchDetailsUseCase(repository)

    private fun fakeDetails(id: String = "launch_1") = LaunchDetailsModel(
        id = id,
        site = "KSC LC-39A",
        missionName = "FalconSat",
        missionPatchUrl = "https://patch.png",
        rocketId = "rocket_1",
        rocketName = "Falcon 1",
        rocketType = "Merlin"
    )

    @Test
    fun `Successful retrieval of launch details`() {
        runTest {
            val id = "launch_1"
            val expected = fakeDetails(id)
            coEvery { repository.getLaunchDetails(id) } returns ResponseState.Success(expected)

            val result = useCase(id)

            assertTrue(result is ResponseState.Success)
            assertEquals(expected, (result as ResponseState.Success).data)
            coVerify(exactly = 1) { repository.getLaunchDetails(id) }
        }
    }

    @Test
    fun `Repository exception propagation`() {
        runTest {
            val id = "launch_1"
            val ex = RuntimeException("boom")
            coEvery { repository.getLaunchDetails(id) } throws ex

            try {
                useCase(id)
                throw AssertionError("Expected exception was not thrown")
            } catch (t: Throwable) {
                assertEquals(ex, t)
            }

            coVerify(exactly = 1) { repository.getLaunchDetails(id) }
        }
    }

    @Test
    fun `Parameter integrity check`() {
        runTest {
            val id = "  launch_1  " // intentionally with spaces
            val expected = fakeDetails(id.trim())
            // We expect useCase NOT to mutate by default, so repo receives exactly id
            coEvery { repository.getLaunchDetails(id) } returns ResponseState.Success(expected)

            val result = useCase(id)

            assertTrue(result is ResponseState.Success)
            coVerify(exactly = 1) { repository.getLaunchDetails(id) }
        }
    }

    @Test
    fun `Handling malformed launchId strings`() {
        runTest {
            val ids = listOf(
                "   ",                // whitespace-only
                "\n\t",               // control whitespace
                "###@@@!!!",          // special chars
                "a".repeat(10_000)    // extremely long
            )

            ids.forEach { id ->
                coEvery { repository.getLaunchDetails(id) } returns ResponseState.Error(
                    NetworkError.RESOURCE_NOT_FOUND, null
                )

                val result = useCase(id)

                assertTrue(result is ResponseState.Error)
                coVerify(exactly = 1) { repository.getLaunchDetails(id) }
            }
        }
    }

    @Test
    fun `Handling repository error response`() {
        runTest {
            val id = "launch_404"

            coEvery { repository.getLaunchDetails(id) } returns ResponseState.Error(
                error = NetworkError.RESOURCE_NOT_FOUND,
                errorBody = null
            )

            val result = useCase(id)

            assertTrue(result is ResponseState.Error)
            val err = result as ResponseState.Error
            assertEquals(NetworkError.RESOURCE_NOT_FOUND, err.error)

            coVerify(exactly = 1) { repository.getLaunchDetails(id) }
        }
    }
}