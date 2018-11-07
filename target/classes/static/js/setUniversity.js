$(function () {
	const contextPath = $('#contextPath').val();
	const $univPref = $('#univPref'),
	$univCategory = $('#univCategory'),
	$univName = $('#univName'),
	$faculty = $('#faculty'),
	$department = $('#department'),
	$gradSchoolPref = $('#gradSchoolPref'),
	$gradSchoolCategory = $('#gradSchoolCategory'),
	$gradSchoolName = $('#gradSchoolName'),
	$gradSchoolOf = $('#gradSchoolOf'),
	$programIn = $('#programIn'),
	$hiddenUnivPref = $('#hiddenUnivPref'),
	$hiddenUnivName = $('#hiddenUnivName'),
	$hiddenFac = $('#hiddenFac'),
	$hiddenDep = $('#hiddenDep')
	$hiddenGradSchoolPref = $('#hiddenGradSchoolPref'),
	$hiddenGradSchoolName = $('#hiddenGradSchoolName'),
	$hiddenGradSchoolOf = $('#hiddenGradSchoolOf'),
	$hiddenProgramIn = $('#hiddenProgramIn');
	let univJson;
	let gradJson;
	const prepJSON = function () {
		$.ajaxSetup({async: false});
		$.getJSON(contextPath + 'js/university.json', function (data) {
			univJson = data;
		});
		$.getJSON(contextPath + 'js/grad_school.json', function (data) {
			gradJson = data;
		});
		$.ajaxSetup({async: true});
	}
	const setUnivCategory = function () {
		$univCategory.children('[value=""]').prop('selected', true);
		$univName.children('[value!=""]').remove();
		$faculty.children('[value!=""]').remove();
		$department.children('[value!=""]').remove();
	}
	const setUnivName = function () {
		$univName.children('[value!=""]').remove();
		$faculty.children('[value!=""]').remove();
		$department.children('[value!=""]').remove();
		if ($univCategory.val() === '') {
			return;
		}
		for (let univ in univJson[$univPref.val()][$univCategory.val()]) {
			let options = '<option value="' + univ + '">' + univ + '</option>';
			$univName.append(options);
		}
	}
	const setFaculty = function () {
		$faculty.children('[value!=""]').remove();
		$department.children('[value!=""]').remove();
		if ($univName.val() === '') {
			return;
		}
		for (let fac in univJson[$univPref.val()][$univCategory.val()][$univName.val()]) {
			let options = '<option value="' + fac + '">' + fac + '</option>';
			$faculty.append(options);
		}
	}
	const setDepartment = function () {
		$department.children('[value!=""]').remove();
		if ($faculty.val() === '') {
			return;
		}
		for (let i = 0; i < univJson[$univPref.val()][$univCategory.val()][$univName.val()][$faculty.val()].length; i++) {
			let dep = univJson[$univPref.val()][$univCategory.val()][$univName.val()][$faculty.val()][i];
			let options = '<option value="' + dep + '">' + dep + '</option>';
			$department.append(options);
		}
	}
	const initUnivPref = function () {
		$univPref.children('[value="' + $hiddenUnivPref.val() + '"]').prop('selected', true);
	}
	const initUnivName = function () {
		if ($hiddenUnivName.val() !== '') {
			let found = false;
			for (let cate in univJson[$univPref.val()]) {
				for (let univ in univJson[$univPref.val()][cate]) {
					if (univ === $hiddenUnivName.val()) {
						$univCategory.val(cate);
						$univCategory.children('[value="' + cate + '"]').prop('selected', true);
						found = true;
						break;
					}
				}
				if (found) {
					break;
				}
			}
		}
		setUnivName();
		$univName.children('[value="' + $hiddenUnivName.val() + '"]').prop('selected', true);
	}
	const initFaculty = function () {
		setFaculty();
		$faculty.children('[value="' + $hiddenFac.val() + '"]').prop('selected', true);
	}
	const initDepartment = function () {
		setDepartment();
		$department.children('[value="' + $hiddenDep.val() + '"]').prop('selected', true);
	}
	
	const setGradSchoolCategory = function () {
		$gradSchoolCategory.children('[value=""]').prop('selected', true);
		$gradSchoolName.children('[value!=""]').remove();
		$gradSchoolOf.children('[value!=""]').remove();
		$programIn.children('[value!=""]').remove();
		if ($gradSchoolPref.val() === '') {
			return;
		}
	}
	const setGradSchoolName = function () {
		$gradSchoolName.children('[value!=""]').remove();
		$gradSchoolOf.children('[value!=""]').remove();
		$programIn.children('[value!=""]').remove();
		if ($gradSchoolCategory.val() === '') {
			return;
		}
		for (let grad in gradJson[$gradSchoolPref.val()][$gradSchoolCategory.val()]) {
			let options = '<option value="' + grad + '">' + grad + '</option>';
			$gradSchoolName.append(options);
		}
	}
	const setGradSchoolOf = function () {
		$gradSchoolOf.children('[value!=""]').remove();
		$programIn.children('[value!=""]').remove();
		if ($gradSchoolName.val() === '') {
			return;
		}
		for (let grad in gradJson[$gradSchoolPref.val()][$gradSchoolCategory.val()][$gradSchoolName.val()]) {
			let options = '<option value="' + grad + '">' + grad + '</option>';
			$gradSchoolOf.append(options);
		}
	}
	const setProgramIn = function () {
		$programIn.children('[value!=""]').remove();
		if ($gradSchoolOf.val() === '') {
			return;
		}
		for (let i = 0; i < gradJson[$gradSchoolPref.val()][$gradSchoolCategory.val()][$gradSchoolName.val()][$gradSchoolOf.val()].length; i++) {
			let grad = gradJson[$gradSchoolPref.val()][$gradSchoolCategory.val()][$gradSchoolName.val()][$gradSchoolOf.val()][i];
			let options = '<option value="' + grad + '">' + grad + '</option>';
			$programIn.append(options);
		}
	}
	const initGradSchoolPref = function () {
		$gradSchoolPref.children('[value="' + $hiddenGradSchoolPref.val() + '"]').prop('selected', true);
	}
	const initGradSchoolName = function () {
		if ($hiddenGradSchoolName.val() !== '') {
			let found = false;
			for (let cate in gradJson[$gradSchoolPref.val()]) {
				for (let grad in gradJson[$gradSchoolPref.val()][cate]) {
					if (grad === $hiddenGradSchoolName.val()) {
						$gradSchoolCategory.val(cate);
						$gradSchoolCategory.children('[value="' + cate + '"]').prop('selected', true);
						found = true;
						break;
					}
				}
				if (found) {
					break;
				}
			}			
		}
		setGradSchoolName();
		$gradSchoolName.children('[value="' + $hiddenGradSchoolName.val() + '"]').prop('selected', true);
	}
	const initGradSchoolOf = function () {
		setGradSchoolOf();
		$gradSchoolOf.children('[value="' + $hiddenGradSchoolOf.val() + '"]').prop('selected', true);
	}
	const initProgramIn = function () {
		setProgramIn();
		$programIn.children('[value="' + $hiddenProgramIn.val() + '"]').prop('selected', true);
	}

	prepJSON();
	initUnivPref();
	initUnivName();
	initFaculty();
	initDepartment();
	initGradSchoolPref();
	initGradSchoolName();
	initGradSchoolOf();
	initProgramIn();
	$univPref.on('input', function (event) {
		event.preventDefault();
		setUnivCategory();
	});
	$univCategory.on('input', function (event) {
		event.preventDefault();
		setUnivName();
	});
	$univName.on('input', function (event) {
		event.preventDefault();
		setFaculty();
	});
	$faculty.on('input', function (event) {
		event.preventDefault();
		setDepartment();
	});
	$gradSchoolPref.on('input', function (event) {
		event.preventDefault();
		setGradSchoolCategory();
	});
	$gradSchoolCategory.on('input', function (event) {
		event.preventDefault();
		setGradSchoolName();
	});
	$gradSchoolName.on('input', function (event) {
		event.preventDefault();
		setGradSchoolOf();
	});
	$gradSchoolOf.on('input', function (event) {
		event.preventDefault();
		setProgramIn();
	});
});