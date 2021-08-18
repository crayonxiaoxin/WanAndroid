package com.github.crayonxiaoxin.wanandroid.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.google.accompanist.permissions.*

/**
 * 申请单个权限
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermission(
    permission: String,
    retry: Boolean = true,
    revoked: () -> Unit = {},
    granted: () -> Unit = {}
) {
    val singlePermissionState = rememberPermissionState(permission)
    PermissionRequired(
        permissionState = singlePermissionState,
        permissionNotGrantedContent = {
            if (singlePermissionState.permissionRequested) {
                revoked()
                if (retry) {
                    SideEffect {
                        singlePermissionState.launchPermissionRequest()
                    }
                }
            }
        },
        permissionNotAvailableContent = {
            if (singlePermissionState.permissionRequested) {
                revoked()
            }
        }
    ) {
        granted()
    }
}

/**
 * 申请多个权限
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermissions(
    permissions: List<String>,
    retry: Boolean = true,
    revoked: (List<String>) -> Unit = {},
    granted: () -> Unit = {}
) {
    val multiPermissionsState = rememberMultiplePermissionsState(
        permissions = permissions
    )
    PermissionsRequired(
        multiplePermissionsState = multiPermissionsState,
        permissionsNotGrantedContent = {
            if (multiPermissionsState.permissionRequested) {
                revoked(multiPermissionsState.revokedPermissions.map { it.permission })
                if (retry) {
                    SideEffect { // 拒絕後，下次打開時再次申請
                        multiPermissionsState.launchMultiplePermissionRequest()
                    }
                }
            }
        },
        permissionsNotAvailableContent = {
            if (multiPermissionsState.permissionRequested) {
                revoked(multiPermissionsState.revokedPermissions.map { it.permission })
            }
        }) {
        granted()
    }
}