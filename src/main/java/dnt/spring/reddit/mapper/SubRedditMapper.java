package dnt.spring.reddit.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import dnt.spring.reddit.dto.SubRedditDto;
import dnt.spring.reddit.model.Post;
import dnt.spring.reddit.model.SubReddit;

@Mapper(componentModel = "spring")
public interface SubRedditMapper {
	
	@Mapping(target = "numberOfPosts", expression = "java(mapPosts(subReddit.getPosts()))")
	SubRedditDto mapSubRedditToDto(SubReddit subReddit);
	
	default Integer mapPosts(List<Post> posts) {
		return posts.size();
	}
	
	@InheritInverseConfiguration
	@Mapping(target = "posts", ignore = true)
	SubReddit mapDtoToSubReddit(SubRedditDto subRedditDto);
}
