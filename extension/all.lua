local REG_SLANLON = '[0-9][0-9][NS][0-9][0-9][0-9][WE]'
local REG_LLANLON = "[0-9][0-9][0-9][0-9][NS][0-9][0-9][0-9][0-9][0-9][WE]"
local REG_ROUTE = '[A-Z]?[A-OQ-Z][0-9]+[A-Z]?'
local REG_ROUTE_EXP = '[A-Z][A-Z][0-9]+[A-Z]?'
local REG_EXTROUTE = '[A-Z]+[0-9]+[A-Z]+'
local REG_SID_STAR = '[A-Z0-9][A-Z0-9]?[A-Z0-9]?[A-Z0-9]?[A-Z0-9]?[A-Z0-9]?[A-Z0-9]?'
local REG_ROUTECHANGE = "([NKM][0-9]+)([FSAM][0-9]+)"
local REG_AP = "[A-Z][A-Z][A-Z][A-Z]"
local REG_POINT = '[A-Z][A-Z][A-Z]?[A-Z]?[A-Z]?'
local REG_NUM = '[0-9]+'
local REG_ACTYPE = "[A-Z0-9][A-Z0-9][A-Z0-9]?[A-Z0-9]?"
local REG_FLTID = "[A-Z0-9][A-Z0-9][A-Z0-9]?[A-Z0-9]?[A-Z0-9]?[A-Z0-9]?[A-Z0-9]?"
local REG_SSR = "[ACS][0-7][0-7][0-7][0-7]"
local TASK_M = { "M" }
local TASK_G = { "G" }
local TASK_N = { "H/Y", "L/W", "N", "H/G", "U/H" }
local TASK_S = { "S", "W/Z" }

function route(obj)
    local fullroute = obj.value
    local routeNum = 0
    local resRoute = ""
    if fullroute then
        local routelist = StrSplit(fullroute, " ")
        local lastroute = false
        local size = #routelist
        for i = 1, size, 1 do
            local inRoute = ""
            if lastroute then
                lastroute = false
            else
                local v = routelist[i]
                if not isempty(v) then
                    if v == "DCT" then
                        if i == 1 then
                            inRoute = inRoute .. "<fx:element seqNum=\"" .. tostring(routeNum) .. "\">"
                            inRoute = inRoute .. '<fx:routeDesignatorToNextElement/>'
                            inRoute = inRoute .. "</fx:element>"
                            routeNum = routeNum + 1
                        end
                    else
                        local name = nil
                        local h_s = nil
                        if string.find(v, "/") then
                            local tmp_arrs = StrSplit(v, "/")
                            name = tmp_arrs[1]
                            h_s = tmp_arrs[2]
                        else
                            name = v
                        end
                        if h_s then
                            inRoute = inRoute .. routeChangeXML(h_s)
                        end
                        if isRoute(name) then
                            inRoute = inRoute .. routeXML(name)
                        elseif isPoint(name) then
                            if routelist[i + 1] then
                                if isRoute(routelist[i + 1])
                                then
                                    inRoute = inRoute .. routeXML(routelist[i + 1])
                                    lastroute = true
                                end
                            end
                            if (inRoute) then
                                inRoute = inRoute .. pointXML(name)
                            end
                        elseif i == 1 and isSIDSTAR(name) then
                            inRoute = inRoute .. sid(name)
                        elseif i == size and isSIDSTAR(name) then
                            inRoute = inRoute .. star(name)
                        elseif isEXTRoute(name) then
                            inRoute = inRoute .. routeXML(name)
                        else
                            local flag, str = isLatLon(name)
                            if flag then
                                inRoute = inRoute .. "<fx:routePoint xsi:type=\"fb:DesignatedPointOrNavaidType\">"
                                inRoute = inRoute .. str .. "</fx:routePoint>"
                            end
                        end
                        if (not isempty(inRoute)) then
                            resRoute = resRoute .. "<fx:element seqNum=\"" .. tostring(routeNum) .. "\">" .. inRoute
                            resRoute = resRoute .. "</fx:element>"
                            routeNum = routeNum + 1
                        end
                    end
                end
            end
        end
    end
    return resRoute
end

function isPoint(point)
    if point then
        if fullMatch(point, REG_POINT) then
            return true
        elseif starts(point, "P") then
            local tail = string.sub(point, 2)
            if fullMatch(tail, REG_NUM) then
                return true
            else
                return false
            end
        end
    end
    return false
end

function isLatLon(point)
    -- Positive	N	E
    -- Negative	S	W
    if point == nil then
        return false, nil
    else
        if string.len(point) == 7 then
            local pstart, pend, str = string.find(point, REG_SLANLON)
            if pstart == 1 and pend == string.len(point) then
                local LAN = string.sub(point, 1, 3)
                local LON = string.sub(point, 4)
                if ends(LAN, "N") then
                    LAN = latCal(string.sub(LAN, 1, 2))
                else
                    LAN = "-" .. latCal(string.sub(LAN, 1, 2))
                end
                if ends(LON, "E") then
                    LON = lonCal(string.sub(LON, 1, 3))
                else
                    LON = "-" .. lonCal(string.sub(LON, 1, 3))
                end
                local res = "<fb:position srsName=\"urn:ogc:def:crs:EPSG::4326\"><fb:pos>"
                --res = res .. latCal(LAN) .. " " .. lonCal(LON)
                res = res .. LAN .. " " .. LON
                res = res .. "</fb:pos></fb:position>"
                return true, res
            end
        elseif string.len(point) == 11
        then
            local pstart, pend = string.find(point, REG_LLANLON)
            if pstart == 1 and pend == string.len(point) then
                local LAN = string.sub(point, 1, 5)
                local LON = string.sub(point, 6)
                if ends(LAN, "N") then
                    LAN = latCal(string.sub(LAN, 1, 4))
                else
                    LAN = "-" .. latCal(string.sub(LAN, 1, 4))
                end
                if ends(LON, "E") then
                    LON = lonCal(string.sub(LON, 1, 5))
                else
                    LON = "-" .. lonCal(string.sub(LON, 1, 5))
                end
                local res = "<fb:position srsName=\"urn:ogc:def:crs:EPSG::4326\"><fb:pos>"
                res = res .. LAN .. " " .. LON
                res = res .. "</fb:pos></fb:position>"
                return true, res
            end
        else
            return false, nil
        end
    end
    return false, nil
end

function lonCal(lonval)
    local res = nil
    if (lonval) then
        if (string.len(lonval) == 5) then
            local int = string.sub(lonval, 1, 3)
            local deci = string.sub(lonval, 4, 5)
            int = tonumber(int)
            deci = tonumber(deci) / 60 + int
            res = string.format("%.4f", deci)
        elseif (string.len(lonval) == 3) then
            local int = string.sub(lonval, 1, 3)
            int = tonumber(int)
            res = string.format("%.4f", int)
        end
    end
    return res
end

function isSIDSTAR(val)
    if (not isempty(val)) then
        if fullMatch(val, REG_SID_STAR) then
            return true
        end
    end
    return false
end

function latCal(lonval)
    local res = nil
    if (lonval) then
        if (string.len(lonval) == 4) then
            local int = string.sub(lonval, 1, 2)
            local deci = string.sub(lonval, 3, 4)
            int = tonumber(int)
            deci = tonumber(deci) / 60 + int
            res = string.format("%.4f", deci)
        elseif (string.len(lonval) == 2) then
            local int = string.sub(lonval, 1, 2)
            int = tonumber(int)
            res = string.format("%.4f", int)
        end
    end
    return res
end

function sid(point)
    local res = ""
    if point then
        res = '<fx:routeDesignatorToNextElement><fx:standardInstrumentDeparture>' .. point
        res = res .. "</fx:standardInstrumentDeparture></fx:routeDesignatorToNextElement>"
    end
    return res
end

function star(point)
    local res = ""
    if point then
        res = '<fx:routeDesignatorToNextElement><fx:standardInstrumentArrival>' .. point
        res = res .. "</fx:standardInstrumentArrival></fx:routeDesignatorToNextElement>"
    end
    return res
end

function isEXTRoute(route)
    if not isempty(route) then
        if fullMatch(route, REG_EXTROUTE) and string.len(route) < 8 then
            return true
        end
    end
    return false
end

function isRoute(route)
    if not isempty(route) then
        if fullMatch(route, REG_ROUTE) or fullMatch(route, REG_ROUTE_EXP) or starts(route, "FANS") then
            return true
        end
    end
    return false
end
function routeChangeXML(change)
    if isempty(change) then
        return nil
    else
        --KESIX/M078F350
        local speed, level = string.match(change, REG_ROUTECHANGE)
        local res = ""
        local seqNum = 0
        if not isempty(speed) then
            res = res .. "<fx:routeChange xsi:type=\"fx:SpeedChangeType\" seqNum=\"" .. seqNum .. "\">"
            local spdtmp = { key = "fx:speed", value = speed }
            res = res .. airspeed(spdtmp)
            res = res .. "</fx:routeChange>"
            seqNum = seqNum + 1
        end
        if not isempty(level) then
            res = res .. "<fx:routeChange xsi:type=\"fx:LevelChangeType\" seqNum=\"" .. seqNum .. "\">"
            local lvltmp = { key = "fb:flightLevel", value = level }
            res = res .. "<fx:level>" .. flightlevel(lvltmp) .. "</fx:level>"
            res = res .. "</fx:routeChange>"
            seqNum = seqNum + 1
        end
        return res
    end
end

function routeXML(routename)
    local res = ""
    if routename then
        res = '<fx:routeDesignatorToNextElement><fx:routeDesignator>' .. routename
        res = res .. "</fx:routeDesignator></fx:routeDesignatorToNextElement>"
    else
        res = '<fx:routeDesignatorToNextElement/>'
    end
    return res
end

function pointXML(point)
    local res = ""
    if (point) then
        res = "<fx:routePoint xsi:type=\"fb:DesignatedPointOrNavaidType\" designator=\"" .. point .. "\"/>"
    else
        res = "<fx:routePoint/>"
    end
    return res
end

function timeformatPro(obj)
    local ori = obj.value
    local key = obj.key
    local res = string.gsub(ori, " ", "T")
    res = key .. "=\"" .. res .. ".00Z\""
    return res
end

function timeformat(obj)
    local ori = obj.value
    local key = obj.key
    local res = string.gsub(ori, " ", "T")
    res = key .. "=\"" .. res .. ".00Z\""
    return res
end

function timeformatNode(obj)
    local ori = obj.value
    local key = obj.key
    local res = string.gsub(ori, " ", "T")
    local res = "<" .. key .. ">" .. res .. ".00Z</" .. key .. ">"
    return res
end

function airport(obj)
    local ap = obj.value
    local res = nil
    if ap and fullMatch(ap, REG_AP) then
        res = "xsi:type=" .. "\"fb:IcaoAerodromeReferenceType\"" .. " locationIndicator=\"" .. ap .. "\""
    end
    return res
end

function apwithSome(obj)
    local ap = obj.value
    local res = nil
    if ap then
        if (string.len(ap) == 4) and fullMatch(atype, REG_AP) then
            res = "xsi:type=\"fb:IcaoAerodromeReferenceType\" locationIndicator=\"" .. ap .. "\""
        elseif (string.len(ap) > 4) then
            res = string.match(ap, REG_AP)
            if res then
                res = "xsi:type=\"fb:IcaoAerodromeReferenceType\" locationIndicator=\"" .. res .. "\""
            end
        end
        return res
    end
    return nil
end

function aircrafttype(obj)
    local atype = obj.value
    local res = nil
    if (atype) then
        local a, i = string.gsub(atype, "[^A-Z0-9]", "")

        local ilen = string.len(a)
        if a and ilen > 2 then
            if string.len(a) > 4 then
                a = string.sub(a, 1, 4)
            end
            res = "xsi:type=\"fx:IcaoAircraftTypeReferenceType\" icaoAircraftTypeDesignator=\"" .. a .. "\""
        end
    end
    return res
end

function registration(obj)
    local reg = obj.value
    local res = nil
    if (reg) then
        local a, i = string.gsub(reg, "[^A-Z0-9]", "")
        if a then
            if string.len(a) > 7 then
                a = string.sub(a, 1, 7)
            end
            res = "registration=\"" .. a .. "\""
        end
    end
    return res
end

function task(obj)
    local atask = obj.value
    if (atask) then
        if inTable(atask, TASK_S) then
            atask = "S"
        elseif inTable(atask, TASK_G) then
            atask = "G"
        elseif inTable(atask, TASK_N) then
            atask = "N"
        elseif inTable(atask, TASK_M) then
            atask = "M"
        else
            atask = "X" -- default others
        end
        return "flightType=\"" .. atask .. "\""
    end
    return nil
end

function pressure(obj)
    local ori = obj.value
    local unit = string.sub(ori, 1, 1)
    local res = nil
    if (unit == "F") then
        res = string.gsub(ori, 'K', 'FL')
    elseif (unit == "S") then
        res = string.gsub(ori, 'S', 'SM')
    elseif (unit == "A") then
        res = string.gsub(ori, 'A', 'FT')
    elseif (unit == "M") then
        res = string.gsub(ori, 'M', 'M')
    end
    return res
end

function frequency(obj)
    local ori = obj.value
    local unit = string.sub(ori, 1, 1)
    local res = nil
    if (unit == "K") then
        res = string.gsub(ori, 'K', 'KHZ')
    elseif (unit == "M") then
        res = string.gsub(ori, 'M', 'MHZ')
    end
    return res
end

function temperature(obj)
    local ori = obj.value
    local unit = string.sub(ori, 1, 1)
    local res = nil
    if (unit == "C") then
        res = string.gsub(ori, 'C', 'C')
    elseif (unit == "F") then
        res = string.gsub(ori, 'F', 'F')
    elseif (unit == "K") then
        res = string.gsub(ori, 'K', 'K')
    elseif (unit == "R") then
        res = string.gsub(ori, 'R', 'R')
    end
    return res
end

function flightlevel(obj)
    local ori = obj.value
    local xmlkey = obj.key
    local unit = string.sub(ori, 1, 1)
    local v = string.sub(ori, 2, string.len(ori))
    local res = "<" .. xmlkey .. " uom=\""
    if (unit == "F") then
        res = res .. "FL"
    elseif (unit == "S" or unit == "M") then
        res = res .. "SM"
    elseif (unit == "A" or unit == "F") then
        res = res .. "FL"
    else
        return nil
    end
    v = tonumber(v)
    res = res .. "\">" .. v .. "</" .. xmlkey .. ">"
    return res
end

function airspeed(obj)
    local ori = obj.value
    local xmlkey = obj.key
    local unit = string.sub(ori, 1, 1)
    local v = string.sub(ori, 2, string.len(ori))
    local res = "<" .. xmlkey .. " uom=\""
    if (unit == "K") then
        res = res .. "KM_H"
    elseif (unit == "N") then
        res = res .. "KT"
    elseif (unit == "M") then
        res = res .. "MACH"
        v = tonumber(v) / 100
    end
    res = res .. "\">"
    res = res .. v
    res = res .. "</" .. xmlkey .. ">"
    return res
end

function distance(obj)
    local ori = obj.value
    local unit = string.sub(ori, 1, 2)
    local key = obj.key
    local v = string.sub(ori, 3, string.len(ori))
    local res = "<" .. key .. " uom=\""
    if (unit == "CM") then
        res = res .. "CM"
    elseif (unit == "FT")
    then
        res = res .. "FT"
    elseif (unit == "IN") then
        res = res .. "IN"
    elseif (unit == "KM") then
        res = res .. "KM"
    elseif (unit == "MI") then
        res = res .. "MI"
    elseif (unit == "MM") then
        res = res .. "MM"
    elseif (unit == "NM") then
        res = res .. "NM"
    else
        -- default M
        res = res .. "M"
    end
    res = res .. "\">"
    res = res .. v
    res = res .. "</" .. key .. ">"
    return res
end

function StrSplit(szFullString, szSeparator)
    local nFindStartIndex = 1
    local nSplitIndex = 1
    local nSplitArray = {}
    while true do
        local nFindLastIndex = string.find(szFullString, szSeparator, nFindStartIndex)
        if not nFindLastIndex then
            nSplitArray[nSplitIndex] = string.sub(szFullString, nFindStartIndex, string.len(szFullString))
            break
        end
        nSplitArray[nSplitIndex] = string.sub(szFullString, nFindStartIndex, nFindLastIndex - 1)
        nFindStartIndex = nFindLastIndex + string.len(szSeparator)
        nSplitIndex = nSplitIndex + 1
    end
    return nSplitArray
end

function inTable(value, table)
    if (table == nil) then
        return false
    end
    for i, v in pairs(table) do
        if (value == v) then
            return true
        end
    end
    return false
end

function starts(String, Start)
    return string.sub(String, 1, string.len(Start)) == Start
end

function ends(String, End)
    return string.sub(String, string.len(String) - string.len(End) + 1, string.len(String)) == End
end

function fullMatch(String, Regex)
    if String then
        local s_start, s_stop = string.find(String, Regex)
        if s_start == 1 and s_stop == string.len(String) then
            return true
        end
    end
    return false
end

function ssr(obj)
    local ori = obj.value
    local res = nil
    if ori and fullMatch(ori, REG_SSR) then
        local ssrmode = string.sub(ori, 1, 1)
        local ssrcode = string.sub(ori, 2, 5)
        res = "<fx:currentSsrCode ssrMode=\"" .. ssrmode .. "\">"
        res = res .. ssrcode .. "</fx:currentSsrCode>"
    end
    return res
end

function flightID(obj)
    local ori = obj.value
    local res = nil
    if (ori) then
        if not fullMatch(ori, REG_FLTID) then
            ori = string.match(ori, REG_FLTID)
        end
        res = "aircraftIdentification=\"" .. ori .. "\""
    end
    return res
end

function isempty(s)
    return s == nil or s == ''
end