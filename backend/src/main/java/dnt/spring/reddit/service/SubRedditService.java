package dnt.spring.reddit.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dnt.spring.reddit.dto.SubRedditDto;
import dnt.spring.reddit.mapper.SubRedditMapper;
import dnt.spring.reddit.model.SubReddit;
import dnt.spring.reddit.repository.SubRedditRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SubRedditService {

	@Autowired
	private SubRedditRepository subRedditRepo;
	
	@Autowired
	private SubRedditMapper subRedditMapper;
	
	@Transactional
	public SubRedditDto createSubReddit(SubRedditDto subRedditDto) {
		SubReddit subReddit = subRedditMapper.mapDtoToSubReddit(subRedditDto);
		subReddit.setCreatedDate(Instant.now());
		subRedditRepo.save(subReddit);
		subRedditDto.setId(subReddit.getId());
		return subRedditDto;
	}

//	private SubReddit mapDtoToSubReddit(SubRedditDto subRedditDto) {
//		return SubReddit.builder().description(subRedditDto.getDescription())
//				.name(subRedditDto.getName()).build();
//	}
//	
//	private SubRedditDto mapSubRedditToDto(SubReddit subReddit) {
//		return SubRedditDto.builder().description(subReddit.getDescription())
//				.name(subReddit.getName())
//				.id(subReddit.getId())
//				.numberOfPosts(subReddit.getPosts().size())
//				.build();
//	}
	
	@Transactional(readOnly = true)
	public List<SubRedditDto> getAll() {
		return subRedditRepo.findAll().stream().map(subRedditMapper::mapSubRedditToDto).collect(Collectors.toList());
	}

	public SubRedditDto getSubRedditById(Long id) {
		return subRedditMapper.mapSubRedditToDto(subRedditRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid Subreddit Id")));
	}

}
