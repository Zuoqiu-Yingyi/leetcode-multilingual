// Copyright (C) 2024 Zuoqiu Yingyi
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU Affero General Public License as
// published by the Free Software Foundation, either version 3 of the
// License, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Affero General Public License for more details.
//
// You should have received a copy of the GNU Affero General Public License
// along with this program.  If not, see <https://www.gnu.org/licenses/>.

import { C } from "./..";

/**
 * 题目 ID 填充前导零
 * @param id - 题目 ID
 * @param maxLength - 填充后的最大长度
 * @returns 填充后的题目 ID
 */
export function idPadZero(
    id: number,
    maxLength = C.ID_LENGTH,
): string {
    return String(id).padStart(maxLength, "0");
}

/**
 * 题目 ID 转换为目录路径
 * @param id - 题目 ID
 * @param prefix - 目录名前缀
 * @param suffix - 目录名后缀
 * @returns 目录路径列表
 */
export function id2paths(
    id: number,
    prefix = "",
    suffix = "",
): string[] {
    return idPadZero(id)
        .split("")
        .map((digit) => `${prefix}${digit}${suffix}`);
}
