/*
 *             Twidere - Twitter client for Android
 *
 *  Copyright (C) 2012-2017 Mariotaku Lee <mariotaku.lee@gmail.com>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mariotaku.twidere.data

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.support.annotation.WorkerThread
import org.mariotaku.twidere.model.SingleResponse

abstract class ExceptionComputableLiveData<T>(loadOnInstantiate: Boolean) : ComputableLiveData<SingleResponse<T>>(loadOnInstantiate) {

    final override fun compute(): SingleResponse<T> {
        return try {
            SingleResponse(data = computeChecked())
        } catch (e: Exception) {
            SingleResponse(exception = e)
        }
    }

    @WorkerThread
    @Throws(Exception::class)
    protected abstract fun computeChecked(): T

    fun observe(owner: LifecycleOwner, success: (T) -> Unit, fail: (Exception) -> Unit = { }) {
        observe(owner, Observer { response ->
            if (response?.data != null) {
                success(response.data)
            } else {
                fail(response?.exception!!)
            }
        })
    }

}
