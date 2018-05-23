local length=#KEYS

local result={}

for i=1,length do
	local key=KEYS[i]
	local list=redis.call("ZRANGE",key,0,0,"WITHSCORES")
	if( (#list > 0) and ((#result == 0 ) or (list[2] < result[2]))) then
      result[1] = list[1]
      result[2] = list[2]
      result[3] = key
   end
end

if( #result > 0 ) then
	redis.call("ZREM", result[3], result[1])
end

return result[1]