package com.spacex.launches.launchesList.domain.useCase

import com.spacex.launches.core.data.remote.NetworkError
import com.spacex.launches.core.data.remote.ResponseState
import com.spacex.launches.launchesList.domain.model.Launch
import com.spacex.launches.launchesList.domain.model.LaunchPage
import com.spacex.launches.launchesList.domain.repository.LaunchesListRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GetLaunchesUseCaseTest {

    private val repository: LaunchesListRepository = mockk()
    private val useCase = GetLaunchesUseCase(repository)

    private fun fakePage(cursor: String? = "c1", hasMore: Boolean = true) = LaunchPage(
        launches = listOf(
            Launch(
                id = "1",
                site = "KSC LC-39A",
                missionName = "FalconSat",
                missionPatch = "https://patch.png",
                rocketName = "Falcon 1"
            )
        ),
        cursor = cursor,
        hasMore = hasMore
    )

    @Test
    fun `invoke success with default parameters`() {
        runTest {
            val expected = fakePage()

            coEvery { repository.getLaunches(null, 20) } returns ResponseState.Success(expected)

            val result = useCase()

            assertTrue(result is ResponseState.Success)
            assertEquals(expected, (result as ResponseState.Success).data)

            coVerify(exactly = 1) { repository.getLaunches(null, 20) }
        }
    }

    @Test
    fun `invoke success with custom cursor and pageSize`() {
        runTest {
            val cursor = "abc"
            val pageSize = 50
            val expected = fakePage(cursor = "next")

            coEvery { repository.getLaunches(cursor, pageSize) } returns ResponseState.Success(expected)

            val result = useCase(cursor, pageSize)

            assertTrue(result is ResponseState.Success)
            assertEquals(expected, (result as ResponseState.Success).data)

            coVerify(exactly = 1) { repository.getLaunches(cursor, pageSize) }
        }
    }

    @Test
    fun `invoke with empty cursor string`() {
        runTest {
            val expected = fakePage()
            coEvery { repository.getLaunches("", 20) } returns ResponseState.Success(expected)

            val result = useCase(cursor = "")

            assertTrue(result is ResponseState.Success)
            assertEquals(expected, (result as ResponseState.Success).data)

            coVerify(exactly = 1) { repository.getLaunches("", 20) }
        }
    }

    @Test
    fun `repository throws unexpected exception`() {
        runTest {
            val ex = RuntimeException("boom")
            coEvery { repository.getLaunches(any(), any()) } throws ex

            try {
                useCase()
                throw AssertionError("Expected exception was not thrown")
            } catch (t: Throwable) {
                assertEquals(ex, t)
            }

            coVerify(exactly = 1) { repository.getLaunches(null, 20) }
        }
    }

    @Test
    fun `invoke with maximum integer pageSize`() {
        runTest {
            val expected = fakePage(cursor = "max")
            coEvery { repository.getLaunches(null, Int.MAX_VALUE) } returns ResponseState.Success(expected)

            val result = useCase(pageSize = Int.MAX_VALUE)

            assertTrue(result is ResponseState.Success)
            assertEquals(expected, (result as ResponseState.Success).data)

            coVerify(exactly = 1) { repository.getLaunches(null, Int.MAX_VALUE) }
        }
    }

    @Test
    fun `repository returns error state`() {
        runTest {
            coEvery { repository.getLaunches(any(), any()) } returns ResponseState.Error(
                error = NetworkError.NO_INTERNET_CONNECTION,
                errorBody = null
            )

            val result = useCase()

            assertTrue(result is ResponseState.Error)
            val err = result as ResponseState.Error
            assertEquals(NetworkError.NO_INTERNET_CONNECTION, err.error)
            assertNull(err.errorBody)

            coVerify(exactly = 1) { repository.getLaunches(null, 20) }
        }
    }

    @Test
    fun `invoke with zero pageSize`() {
        runTest {
            coEvery { repository.getLaunches(null, 0) } returns ResponseState.Error(
                error = NetworkError.CLIENT_API_ERROR,
                errorBody = null
            )

            val result = useCase(pageSize = 0)

            assertTrue(result is ResponseState.Error)
            val err = result as ResponseState.Error
            assertEquals(NetworkError.CLIENT_API_ERROR, err.error)

            coVerify(exactly = 1) { repository.getLaunches(null, 0) }
        }
    }

    @Test
    fun `invoke with negative pageSize`() {
        runTest {
            coEvery { repository.getLaunches(null, -1) } returns ResponseState.Error(
                error = NetworkError.CLIENT_API_ERROR,
                errorBody = null
            )

            val result = useCase(pageSize = -1)

            assertTrue(result is ResponseState.Error)
            val err = result as ResponseState.Error
            assertEquals(NetworkError.CLIENT_API_ERROR, err.error)

            coVerify(exactly = 1) { repository.getLaunches(null, -1) }
        }
    }
}